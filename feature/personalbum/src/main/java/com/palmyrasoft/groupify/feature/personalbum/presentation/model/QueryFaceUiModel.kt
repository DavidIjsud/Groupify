package com.palmyrasoft.groupify.feature.personalbum.presentation.model

import com.palmyrasoft.groupify.feature.personalbum.domain.model.BoundingBox

data class QueryFaceUiModel(
    val id: Int,
    val label: String,
    val boundingBox: BoundingBox,
    val isSelected: Boolean,
    val thumbnailUri: String? = null,
)
