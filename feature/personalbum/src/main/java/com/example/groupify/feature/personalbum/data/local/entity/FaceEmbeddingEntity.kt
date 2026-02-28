// feature/personalbum/src/main/.../data/local/entity/FaceEmbeddingEntity.kt
package com.example.groupify.feature.personalbum.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Suppress("ArrayInDataClass")
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
    val embedding: FloatArray,
    val createdAt: Long,
)
