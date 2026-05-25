// feature/personalbum/src/main/.../data/local/entity/GroupEntity.kt
package com.palmyrasoft.groupify.feature.personalbum.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey val id: String,
    val name: String,
    val faceCount: Int,
    val createdAt: Long,
    val updatedAt: Long,
)
