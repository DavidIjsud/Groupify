// feature/personalbum/src/main/.../domain/model/PhotoEmbedding.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.model

/**
 * The CLIP semantic embedding of a single gallery photo, kept in the on-device description index so
 * the user can find photos by a typed description (signs, scenes, objects…). Custom equals/hashCode
 * because [embedding] is a FloatArray.
 */
data class PhotoEmbedding(
    val photoId: String,
    val uri: String,
    val embedding: FloatArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PhotoEmbedding) return false
        return photoId == other.photoId &&
            uri == other.uri &&
            embedding.contentEquals(other.embedding)
    }

    override fun hashCode(): Int {
        var result = photoId.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + embedding.contentHashCode()
        return result
    }
}
