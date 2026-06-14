// feature/personalbum/src/main/.../domain/usecase/SearchByDescriptionUseCase.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.usecase

import com.palmyrasoft.groupify.feature.personalbum.domain.model.PhotoMatch
import com.palmyrasoft.groupify.feature.personalbum.domain.recognition.TextQueryEmbedder
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.DescriptionIndexRepository
import com.palmyrasoft.groupify.feature.personalbum.domain.util.cosineSimilarity
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Finds gallery photos that best match a typed natural-language description, via CLIP semantic
 * search. The description-search counterpart of
 * [com.palmyrasoft.groupify.feature.personalbum.domain.usecase.SearchByPhotoUseCase]: embed the
 * query into the CLIP text space, then cosine-rank it against every stored photo embedding.
 *
 * A blank query yields no results. Unlike face search there is no per-photo margin test — every
 * photo has exactly one embedding — so we keep the top [MAX_RESULTS] above [DEFAULT_MIN_SCORE].
 */
class SearchByDescriptionUseCase @Inject constructor(
    private val textQueryEmbedder: TextQueryEmbedder,
    private val descriptionIndexRepository: DescriptionIndexRepository,
) {
    suspend operator fun invoke(
        query: String,
        minScore: Float = DEFAULT_MIN_SCORE,
        limit: Int = MAX_RESULTS,
    ): List<PhotoMatch> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return emptyList()

        val stored = descriptionIndexRepository.getAllEmbeddings().first()
        if (stored.isEmpty()) return emptyList()

        val queryEmbedding = textQueryEmbedder.embedQuery(trimmed)

        return stored
            .map { photo ->
                PhotoMatch(
                    uri = photo.uri,
                    score = cosineSimilarity(queryEmbedding, photo.embedding).coerceIn(-1f, 1f),
                )
            }
            .filter { it.score >= minScore }
            .sortedByDescending { it.score }
            .take(limit)
    }

    companion object {
        /**
         * CLIP image↔text cosine similarities are not temperature-scaled here, so matching pairs
         * land around ~0.25–0.4 and unrelated ones near ~0.0–0.15. This floor drops obviously
         * unrelated photos; tune on-device if results feel too sparse or too noisy.
         */
        const val DEFAULT_MIN_SCORE = 0.2f

        /** Cap the grid so a vague query doesn't render the entire gallery. */
        const val MAX_RESULTS = 60
    }
}
