// feature/personalbum/src/main/.../data/local/entity/PhotoEmbeddingEntity.kt
package com.palmyrasoft.groupify.feature.personalbum.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The CLIP semantic embedding of one gallery photo, for the description-search flow. One row per
 * photo (keyed by photoId), so re-indexing the same photo REPLACEs its vector. [embeddingBlob] is a
 * FloatArray serialized via [com.palmyrasoft.groupify.feature.personalbum.data.local.Converters].
 */
@Entity(tableName = "photo_embeddings")
data class PhotoEmbeddingEntity(
    @PrimaryKey val photoId: String,
    val uri: String,
    val embeddingBlob: ByteArray,
    val createdAt: Long,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PhotoEmbeddingEntity) return false
        return photoId == other.photoId &&
            uri == other.uri &&
            embeddingBlob.contentEquals(other.embeddingBlob) &&
            createdAt == other.createdAt
    }

    override fun hashCode(): Int {
        var result = photoId.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + embeddingBlob.contentHashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}
