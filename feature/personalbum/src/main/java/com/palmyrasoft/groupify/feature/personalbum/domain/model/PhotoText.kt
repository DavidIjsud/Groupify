// feature/personalbum/src/main/.../domain/model/PhotoText.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.model

/**
 * The text recognized inside a single gallery photo, kept in the on-device text index so the
 * user can find photos that visually contain a word or phrase (signs, screenshots, receipts…).
 */
data class PhotoText(
    val photoId: String,
    val uri: String,
    val text: String,
)
