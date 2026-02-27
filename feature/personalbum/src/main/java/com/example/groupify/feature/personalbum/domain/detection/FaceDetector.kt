// feature/personalbum/src/main/.../domain/detection/FaceDetector.kt
package com.example.groupify.feature.personalbum.domain.detection

import com.example.groupify.feature.personalbum.domain.model.DetectedFace

interface FaceDetector {
    suspend fun detectFaces(photoUri: String): List<DetectedFace>
}
