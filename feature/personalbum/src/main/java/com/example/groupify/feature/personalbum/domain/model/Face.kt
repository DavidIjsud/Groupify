// feature/personalbum/src/main/.../domain/model/Face.kt
package com.example.groupify.feature.personalbum.domain.model

data class BoundingBox(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
)

data class Face(
    val photoId: String,
    val boundingBox: BoundingBox,
    val embedding: FloatArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Face) return false
        return photoId == other.photoId &&
            boundingBox == other.boundingBox &&
            embedding.contentEquals(other.embedding)
    }

    override fun hashCode(): Int {
        var result = photoId.hashCode()
        result = 31 * result + boundingBox.hashCode()
        result = 31 * result + embedding.contentHashCode()
        return result
    }
}
