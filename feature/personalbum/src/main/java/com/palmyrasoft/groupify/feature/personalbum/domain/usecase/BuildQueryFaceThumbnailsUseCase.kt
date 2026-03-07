package com.palmyrasoft.groupify.feature.personalbum.domain.usecase

import com.palmyrasoft.groupify.feature.personalbum.domain.model.QueryFace
import com.palmyrasoft.groupify.feature.personalbum.domain.thumbnail.QueryFaceThumbnailGenerator
import javax.inject.Inject

class BuildQueryFaceThumbnailsUseCase @Inject constructor(
    private val generator: QueryFaceThumbnailGenerator,
) {
    suspend operator fun invoke(
        queryPhotoUri: String,
        faces: List<QueryFace>,
    ): Map<Int, String> = generator.generate(queryPhotoUri, faces)
}
