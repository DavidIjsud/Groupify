// feature/personalbum/src/main/.../data/source/AndroidMediaStorePhotoDataSource.kt
package com.example.groupify.feature.personalbum.data.source

import android.content.Context
import com.example.groupify.feature.personalbum.domain.model.Photo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AndroidMediaStorePhotoDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun queryPhotos(): Flow<List<Photo>> = TODO("Query MediaStore.Images.Media and map to Photo")
}
