package com.example.groupify.feature.personalbum.domain.thumbnail

import com.example.groupify.feature.personalbum.domain.model.QueryFace

/**
 * Port: generates cropped face thumbnails from a query photo.
 * Returns a map of faceId -> content URI string (FileProvider) for each face.
 */
interface QueryFaceThumbnailGenerator {
    suspend fun generate(queryPhotoUri: String, faces: List<QueryFace>): Map<Int, String>
}
