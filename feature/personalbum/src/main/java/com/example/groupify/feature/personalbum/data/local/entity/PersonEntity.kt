// feature/personalbum/src/main/.../data/local/entity/PersonEntity.kt
package com.example.groupify.feature.personalbum.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey val id: String,
    val name: String,
    val referenceEmbeddingBlob: ByteArray,
    val createdAt: Long,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PersonEntity) return false
        return id == other.id &&
            name == other.name &&
            referenceEmbeddingBlob.contentEquals(other.referenceEmbeddingBlob) &&
            createdAt == other.createdAt
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + referenceEmbeddingBlob.contentHashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}
