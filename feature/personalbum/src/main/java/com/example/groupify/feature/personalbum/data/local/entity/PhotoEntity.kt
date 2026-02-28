// feature/personalbum/src/main/.../data/local/entity/PhotoEntity.kt
package com.example.groupify.feature.personalbum.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey val id: String,
    val uri: String,
    val dateTaken: Long,
    val lastIndexedAt: Long?,
)
