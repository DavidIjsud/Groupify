// feature/personalbum/src/main/.../domain/usecase/SearchByTextUseCase.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.usecase

import com.palmyrasoft.groupify.feature.personalbum.domain.model.PhotoMatch
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.PhotoTextRepository
import javax.inject.Inject

/**
 * Finds gallery photos whose recognized text contains the user's query.
 *
 * The text-search counterpart of
 * [com.palmyrasoft.groupify.feature.personalbum.domain.usecase.SearchByPhotoUseCase]. A blank
 * query yields no results; the FTS query construction itself lives in the data layer.
 */
class SearchByTextUseCase @Inject constructor(
    private val photoTextRepository: PhotoTextRepository,
) {
    suspend operator fun invoke(query: String): List<PhotoMatch> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return emptyList()
        return photoTextRepository.search(trimmed)
    }
}
