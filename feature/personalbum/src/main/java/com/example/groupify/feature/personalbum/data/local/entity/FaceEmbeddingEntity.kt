// feature/personalbum/src/main/.../data/local/entity/FaceEmbeddingEntity.kt
package com.example.groupify.feature.personalbum.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "face_embeddings",
    indices = [Index(value = ["photoId"])],
)
data class FaceEmbeddingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val photoId: String,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val embeddingBlob: ByteArray,
    val createdAt: Long,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FaceEmbeddingEntity) return false
        return id == other.id &&
            photoId == other.photoId &&
            left == other.left &&
            top == other.top &&
            right == other.right &&
            bottom == other.bottom &&
            embeddingBlob.contentEquals(other.embeddingBlob) &&
            createdAt == other.createdAt
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + photoId.hashCode()
        result = 31 * result + left.hashCode()
        result = 31 * result + top.hashCode()
        result = 31 * result + right.hashCode()
        result = 31 * result + bottom.hashCode()
        result = 31 * result + embeddingBlob.contentHashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}
