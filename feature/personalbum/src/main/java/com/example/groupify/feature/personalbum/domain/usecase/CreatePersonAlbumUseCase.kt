// feature/personalbum/src/main/.../domain/usecase/CreatePersonAlbumUseCase.kt
package com.example.groupify.feature.personalbum.domain.usecase

import com.example.groupify.feature.personalbum.domain.detection.FaceDetector
import com.example.groupify.feature.personalbum.domain.model.Person
import com.example.groupify.feature.personalbum.domain.recognition.FaceEmbedder
import com.example.groupify.feature.personalbum.domain.repository.PersonRepository
import java.util.UUID
import javax.inject.Inject

class CreatePersonAlbumUseCase @Inject constructor(
    private val personRepository: PersonRepository,
    private val faceDetector: FaceDetector,
    private val faceEmbedder: FaceEmbedder,
) {
    suspend operator fun invoke(name: String, referencePhotoUri: String): Person {
        val detectedFaces = faceDetector.detectFaces(referencePhotoUri)
        require(detectedFaces.isNotEmpty()) { "No face detected in reference photo" }

        val largestFace = detectedFaces.maxBy { face ->
            val bb = face.boundingBox
            (bb.right - bb.left) * (bb.bottom - bb.top)
        }

        val embedding = faceEmbedder.embedFace(referencePhotoUri, largestFace.boundingBox)

        val person = Person(
            id = UUID.randomUUID().toString(),
            name = name,
            referenceEmbedding = embedding,
        )
        personRepository.save(person)
        return person
    }
}
