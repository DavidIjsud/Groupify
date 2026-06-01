// feature/personalbum/src/main/.../data/local/entity/PhotoTextEntity.kt
package com.palmyrasoft.groupify.feature.personalbum.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

/**
 * Full-text-searchable store of the text recognized inside each gallery photo.
 *
 * Declared as an FTS4 table so "search images that contain <text>" runs as a fast, tokenized
 * `MATCH` query rather than a `LIKE '%…%'` table scan. FTS tables expose an implicit INTEGER
 * `rowid`; Room requires it to be the (auto-assigned) primary key.
 *
 * Dedup is not enforced by FTS, but indexing only OCRs each photo once (gated by
 * PhotoEntity.lastIndexedAt), and DB upgrades wipe the table (fallbackToDestructiveMigration),
 * so duplicates don't accumulate in normal use.
 */
@Fts4
@Entity(tableName = "photo_texts")
data class PhotoTextEntity(
    @PrimaryKey @ColumnInfo(name = "rowid") val rowId: Int = 0,
    val photoId: String,
    val uri: String,
    val text: String,
)
