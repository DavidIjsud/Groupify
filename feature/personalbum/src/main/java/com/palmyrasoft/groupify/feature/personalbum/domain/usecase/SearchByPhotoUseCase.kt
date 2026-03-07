package com.palmyrasoft.groupify.feature.personalbum.domain.usecase

import com.palmyrasoft.groupify.feature.personalbum.domain.model.BoundingBox
import com.palmyrasoft.groupify.feature.personalbum.domain.model.PhotoMatch
import com.palmyrasoft.groupify.feature.personalbum.domain.recognition.FaceEmbedder
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.PhotoRepository
import com.palmyrasoft.groupify.feature.personalbum.domain.util.cosineSimilarity
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SearchByPhotoUseCase @Inject constructor(
    private val faceEmbedder: FaceEmbedder,
    private val faceIndexRepository: FaceIndexRepository,
    private val photoRepository: PhotoRepository,
) {
    /**
     * @param queryPhotoUri  URI of the photo whose faces are being searched.
     * @param selectedFaces  Bounding boxes of the query faces the user selected.
     * @param threshold      Minimum cosine similarity for a stored face to be considered a match.
     * @param margin         Minimum gap between the best and second-best similarity scores for a
     *                       given (query face, photo) pair. Rejects ambiguous matches where two
     *                       different stored faces in the same photo score almost equally well
     *                       against the query, which is a reliable false-positive signal.
     *                       Defaults to [DEFAULT_MARGIN]. Single-face photos always pass because
     *                       their second-best score is Float.NEGATIVE_INFINITY.
     */
    suspend operator fun invoke(
        queryPhotoUri: String,
        selectedFaces: List<BoundingBox>,
        threshold: Float,
        margin: Float = DEFAULT_MARGIN,
    ): List<PhotoMatch> {
        require(selectedFaces.isNotEmpty()) { "Select at least one face to search" }

        val storedFaces = faceIndexRepository.getAllFaces().first()
        if (storedFaces.isEmpty()) {
            throw IllegalStateException("No indexed faces yet. Run indexing first.")
        }

        // bestScoreByPhoto accumulates the highest qualifying score across all selected query
        // faces. A photo ends up here only after passing both the threshold and the margin test.
        val bestScoreByPhoto = HashMap<String, Float>()

        for (boundingBox in selectedFaces) {
            val queryEmbedding = faceEmbedder.embedFace(queryPhotoUri, boundingBox)

            // For this query face, scan every stored embedding and maintain the top-2
            // similarity scores per photoId without any Pair/object allocations.
            //
            // bestByPhoto[id]   = highest sim seen so far for that photoId
            // secondByPhoto[id] = second-highest sim (NEGATIVE_INFINITY when only one seen)
            val bestByPhoto = HashMap<String, Float>()
            val secondByPhoto = HashMap<String, Float>()

            for (storedFace in storedFaces) {
                val sim = cosineSimilarity(queryEmbedding, storedFace.embedding)
                    .coerceIn(-1f, 1f)
                val id = storedFace.photoId
                val prev = bestByPhoto.getOrDefault(id, Float.NEGATIVE_INFINITY)
                if (sim > prev) {
                    secondByPhoto[id] = prev      // old best becomes second
                    bestByPhoto[id] = sim
                } else {
                    val prevSecond = secondByPhoto.getOrDefault(id, Float.NEGATIVE_INFINITY)
                    if (sim > prevSecond) {
                        secondByPhoto[id] = sim
                    }
                }
            }

            // Apply threshold + margin filter and promote passing photos into the shared result.
            for ((photoId, bestSim) in bestByPhoto) {
                if (bestSim < threshold) continue
                val secondSim = secondByPhoto.getOrDefault(photoId, Float.NEGATIVE_INFINITY)
                if (bestSim - secondSim < margin) continue  // ambiguous — two faces too close

                val current = bestScoreByPhoto[photoId]
                if (current == null || bestSim > current) {
                    bestScoreByPhoto[photoId] = bestSim
                }
            }
        }

        if (bestScoreByPhoto.isEmpty()) return emptyList()

        val photos = photoRepository.getByIds(bestScoreByPhoto.keys.toList())
        val photoMap = photos.associateBy { it.id }

        return bestScoreByPhoto.entries
            .mapNotNull { (photoId, score) ->
                val uri = photoMap[photoId]?.uri ?: return@mapNotNull null
                if (uri == queryPhotoUri) return@mapNotNull null
                PhotoMatch(uri = uri, score = score)
            }
            .sortedByDescending { it.score }
    }

    companion object {
        /** Minimum best-vs-second-best gap required to accept a match as unambiguous. */
        const val DEFAULT_MARGIN = 0.06f
    }
}
