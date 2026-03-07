// feature/personalbum/src/main/.../domain/model/DetectedFace.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.model

data class DetectedFace(
    val boundingBox: BoundingBox,
    val trackingId: Int?,
    val smilingProbability: Float?,
    val leftEyeOpenProbability: Float?,
    val rightEyeOpenProbability: Float?,
)
