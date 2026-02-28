// feature/personalbum/src/main/.../data/repository/PersonRepositoryImpl.kt
package com.example.groupify.feature.personalbum.data.repository

import com.example.groupify.feature.personalbum.data.local.Converters
import com.example.groupify.feature.personalbum.data.local.dao.PersonDao
import com.example.groupify.feature.personalbum.data.local.entity.PersonEntity
import com.example.groupify.feature.personalbum.domain.model.Person
import com.example.groupify.feature.personalbum.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val personDao: PersonDao,
) : PersonRepository {

    override suspend fun save(person: Person) {
        personDao.insert(person.toEntity())
    }

    override fun getById(id: String): Flow<Person?> =
        personDao.getById(id).map { it?.toDomain() }

    override fun getAll(): Flow<List<Person>> =
        personDao.getAll().map { entities -> entities.map { it.toDomain() } }
}

private fun Person.toEntity(): PersonEntity = PersonEntity(
    id = id,
    name = name,
    referenceEmbeddingBlob = Converters.floatArrayToByteArray(referenceEmbedding),
    createdAt = System.currentTimeMillis(),
)

private fun PersonEntity.toDomain(): Person = Person(
    id = id,
    name = name,
    referenceEmbedding = Converters.byteArrayToFloatArray(referenceEmbeddingBlob),
)
