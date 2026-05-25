// feature/personalbum/src/main/.../domain/model/Group.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.model

/**
 * A user-saved collection of matched photos.
 *
 * A group is a *reference store* only: it holds the content URIs of photos that already live
 * in the gallery — deleting a group never deletes the underlying photos.
 *
 * @param photoUris content URIs of the photos in the group, newest-added first.
 * @param faceCount the number of query faces that were searched when the group was created
 *                  (carried forward as max(existing, new) when photos are added).
 */
data class Group(
    val id: String,
    val name: String,
    val photoUris: List<String>,
    val faceCount: Int,
    val createdAt: Long,
    val updatedAt: Long,
) {
    val photoCount: Int get() = photoUris.size
}

/** Case-insensitive, trimmed normal form used for duplicate-name checks. */
fun String.normalizedGroupName(): String = trim().lowercase()
