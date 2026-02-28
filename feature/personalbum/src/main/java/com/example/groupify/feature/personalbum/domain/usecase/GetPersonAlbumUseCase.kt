// feature/personalbum/src/main/java/com/example/groupify/feature/personalbum/domain/usecase/GetPersonAlbumUseCase.kt
package com.example.groupify.feature.personalbum.domain.usecase

import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.example.groupify.feature.personalbum.domain.repository.PersonRepository
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository
import com.example.groupify.feature.personalbum.domain.util.cosineSimilarity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPersonAlbumUseCase @Inject constructor(
    private val personRepository: PersonRepository,
    private val faceIndexRepository: FaceIndexRepository,
    private val photoRepository: PhotoRepository,
) {
    operator fun invoke(personId: String, threshold: Float = 0.75f): Flow<List<String>> = flow {
        val person = personRepository.getById(personId).first()
            ?: throw IllegalStateException("Person not found: $personId")

        val allFaces = faceIndexRepository.getAllFaces().first()

        val matchedPhotoIds = allFaces
            .filter { face ->
                cosineSimilarity(person.referenceEmbedding, face.embedding) >= threshold
            }
            .map { it.photoId }
            .distinct()

        val photoMap = photoRepository.getByIds(matchedPhotoIds).associateBy { it.id }
        val uris = matchedPhotoIds.mapNotNull { photoMap[it]?.uri }

        emit(uris)
    }
}
