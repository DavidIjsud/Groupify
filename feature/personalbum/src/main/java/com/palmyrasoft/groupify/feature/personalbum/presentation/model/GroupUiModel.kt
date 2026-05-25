package com.palmyrasoft.groupify.feature.personalbum.presentation.model

/** Lightweight group representation for list cards and the create-sheet "add to existing" list. */
data class GroupUiModel(
    val id: String,
    val name: String,
    val photoCount: Int,
    val faceCount: Int,
    val updatedAt: Long,
    /** Up to 4 URIs for the 2×2 collage / stacked preview. */
    val previewUris: List<String>,
)

/** Full group with all photo URIs, for the detail screen. */
data class GroupDetailUiModel(
    val id: String,
    val name: String,
    val photoCount: Int,
    val faceCount: Int,
    val photoUris: List<String>,
)
