// feature/personalbum/src/main/.../data/local/relation/GroupWithPhotos.kt
package com.palmyrasoft.groupify.feature.personalbum.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.GroupEntity
import com.palmyrasoft.groupify.feature.personalbum.data.local.entity.GroupPhotoEntity

data class GroupWithPhotos(
    @Embedded val group: GroupEntity,
    @Relation(parentColumn = "id", entityColumn = "groupId")
    val photos: List<GroupPhotoEntity>,
)
