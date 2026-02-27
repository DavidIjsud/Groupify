// feature/personalbum/src/main/.../domain/usecase/CreatePersonAlbumUseCase.kt
package com.example.groupify.feature.personalbum.domain.usecase

import com.example.groupify.feature.personalbum.domain.model.Person
import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.example.groupify.feature.personalbum.domain.repository.PersonRepository
import javax.inject.Inject

class CreatePersonAlbumUseCase @Inject constructor(
    private val personRepository: PersonRepository,
    private val faceIndexRepository: FaceIndexRepository,
) {
    suspend operator fun invoke(name: String, referenceEmbedding: FloatArray): Person =
        TODO("Create Person, persist, return it")
}
