// feature/personalbum/src/main/.../data/source/AndroidMediaStorePhotoDataSource.kt
package com.example.groupify.feature.personalbum.data.source

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.example.groupify.feature.personalbum.domain.model.Photo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AndroidMediaStorePhotoDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun queryPhotos(limit: Int = 1000): Flow<List<Photo>> = flow {
        val photos = withContext(Dispatchers.IO) {
            val result = mutableListOf<Photo>()

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATE_ADDED,
            )
            val sortOrder =
                "${MediaStore.Images.Media.DATE_TAKEN} DESC, ${MediaStore.Images.Media.DATE_ADDED} DESC"

            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder,
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dateTakenColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
                val dateAddedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

                var count = 0
                while (cursor.moveToNext() && count < limit) {
                    val id = cursor.getLong(idColumn)
                    val dateTaken = cursor.getLong(dateTakenColumn)
                    val dateAdded = cursor.getLong(dateAddedColumn)

                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id,
                    )

                    result.add(
                        Photo(
                            id = id.toString(),
                            uri = contentUri.toString(),
                            dateTaken = if (dateTaken > 0) dateTaken else dateAdded * 1000L,
                        )
                    )
                    count++
                }
            }

            result
        }
        emit(photos)
    }
}
