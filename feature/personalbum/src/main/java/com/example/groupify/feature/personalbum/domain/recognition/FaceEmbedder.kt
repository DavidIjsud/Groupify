// feature/personalbum/src/main/.../domain/recognition/FaceEmbedder.kt
package com.example.groupify.feature.personalbum.domain.recognition

import com.example.groupify.feature.personalbum.domain.model.BoundingBox

interface FaceEmbedder {
    suspend fun embedFace(photoUri: String, faceBoundingBox: BoundingBox): FloatArray
}
