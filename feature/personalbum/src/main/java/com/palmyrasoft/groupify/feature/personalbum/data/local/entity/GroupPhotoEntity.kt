// feature/personalbum/src/main/.../data/local/entity/GroupPhotoEntity.kt
package com.palmyrasoft.groupify.feature.personalbum.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * A photo reference belonging to a group. Stores only the content URI — the photo itself
 * stays in the gallery. The unique (groupId, photoUri) index makes "add to group" dedupe
 * for free via [androidx.room.OnConflictStrategy.IGNORE]; the foreign key cascades deletes
 * so removing a group drops its photo references (never the photos).
 */
@Entity(
    tableName = "group_photos",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["groupId"]),
        Index(value = ["groupId", "photoUri"], unique = true),
    ],
)
data class GroupPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val groupId: String,
    val photoUri: String,
    val addedAt: Long,
)
