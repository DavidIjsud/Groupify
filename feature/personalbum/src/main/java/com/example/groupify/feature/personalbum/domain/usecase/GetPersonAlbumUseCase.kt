// feature/personalbum/src/main/.../domain/usecase/GetPersonAlbumUseCase.kt
package com.example.groupify.feature.personalbum.domain.usecase

import com.example.groupify.feature.personalbum.domain.model.Photo
import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.example.groupify.feature.personalbum.domain.repository.PersonRepository
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPersonAlbumUseCase @Inject constructor(
    private val personRepository: PersonRepository,
    private val photoRepository: PhotoRepository,
    private val faceIndexRepository: FaceIndexRepository,
) {
    operator fun invoke(personId: String): Flow<List<Photo>> =
        TODO("Match faces by personId embedding, return associated photos")
}
