package com.example.groupify.feature.personalbum.presentation.model

import com.example.groupify.feature.personalbum.domain.model.BoundingBox

data class QueryFaceUiModel(
    val id: Int,
    val label: String,
    val boundingBox: BoundingBox,
    val isSelected: Boolean,
)
