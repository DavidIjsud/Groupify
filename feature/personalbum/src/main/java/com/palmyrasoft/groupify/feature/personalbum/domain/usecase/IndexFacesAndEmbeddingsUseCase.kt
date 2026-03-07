// feature/personalbum/src/main/.../domain/usecase/IndexFacesAndEmbeddingsUseCase.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.usecase

import android.os.SystemClock
import android.util.Log
import com.palmyrasoft.groupify.feature.personalbum.BuildConfig
import com.palmyrasoft.groupify.feature.personalbum.domain.detection.FaceDetector
import com.palmyrasoft.groupify.feature.personalbum.domain.model.Face
import com.palmyrasoft.groupify.feature.personalbum.domain.recognition.FaceEmbedder
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IndexFacesAndEmbeddingsUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val faceIndexRepository: FaceIndexRepository,
    private val faceDetector: FaceDetector,
    private val faceEmbedder: FaceEmbedder,
) {
    operator fun invoke(): Flow<IndexingProgress> = flow {
        val allPhotos = photoRepository.getAll().first()
        photoRepository.upsertAll(allPhotos)

        val unindexed = photoRepository.getUnindexed(limit = allPhotos.size)
        val total = unindexed.size

        // Optimization (3): accumulate faces and photo IDs across multiple photos before
        // writing to Room.  Each insertAll() and UPDATE … IN (…) call starts a SQLite
        // transaction; doing one per BATCH_SIZE photos instead of one per photo gives a
        // ~50x reduction in transaction count for typical libraries.
        //
        // Progress is still emitted per photo so the UI ticks smoothly even during large
        // batches.  Only the DB flush is deferred.
        val faceBatch = mutableListOf<Face>()
        val indexedIdsBatch = mutableListOf<String>()

        unindexed.forEachIndexed { index, photo ->
            val tPhoto = if (BuildConfig.DEBUG) SystemClock.elapsedRealtime() else 0L

            try {
                val tDetect = if (BuildConfig.DEBUG) SystemClock.elapsedRealtime() else 0L
                val detectedFaces = faceDetector.detectFaces(photo.uri)
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "detectFaces [${photo.id}] → ${detectedFaces.size} face(s) in " +
                        "${SystemClock.elapsedRealtime() - tDetect}ms")
                }

                for (detectedFace in detectedFaces) {
                    try {
                        val tEmbed = if (BuildConfig.DEBUG) SystemClock.elapsedRealtime() else 0L
                        val embedding = faceEmbedder.embedFace(photo.uri, detectedFace.boundingBox)
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "embedFace [${photo.id}] in ${SystemClock.elapsedRealtime() - tEmbed}ms")
                        }
                        faceBatch.add(
                            Face(
                                photoId = photo.id,
                                boundingBox = detectedFace.boundingBox,
                                embedding = embedding,
                            )
                        )
                    } catch (e: Exception) {
                        // Skip this face if embedding fails — don't abort the whole photo.
                    }
                }

                // Photo processed successfully — queue it for the batched markIndexed call.
                indexedIdsBatch.add(photo.id)
            } catch (e: Exception) {
                // Photo-level failure: do NOT add to indexedIdsBatch so it gets retried next run.
            }

            // Flush to DB every BATCH_SIZE photos (or on the final photo).
            val isFinal = index == unindexed.lastIndex
            if (indexedIdsBatch.size >= BATCH_SIZE || (isFinal && indexedIdsBatch.isNotEmpty())) {
                val tDb = if (BuildConfig.DEBUG) SystemClock.elapsedRealtime() else 0L

                if (faceBatch.isNotEmpty()) {
                    faceIndexRepository.saveAll(faceBatch)
                    faceBatch.clear()
                }

                val timestamp = System.currentTimeMillis()
                photoRepository.markAllIndexed(indexedIdsBatch, timestamp)

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "DB batch flush: ${indexedIdsBatch.size} photos marked indexed in " +
                        "${SystemClock.elapsedRealtime() - tDb}ms")
                }

                indexedIdsBatch.clear()
            }

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "photo ${index + 1}/$total processed in ${SystemClock.elapsedRealtime() - tPhoto}ms total")
            }

            emit(IndexingProgress(current = index + 1, total = total))
        }
    }

    companion object {
        private const val TAG = "IndexFacesUseCase"

        // Flush every 50 photos — balances transaction overhead vs. crash-recovery granularity.
        // Reduce if you observe memory pressure; increase for very large libraries.
        private const val BATCH_SIZE = 50
    }
}
