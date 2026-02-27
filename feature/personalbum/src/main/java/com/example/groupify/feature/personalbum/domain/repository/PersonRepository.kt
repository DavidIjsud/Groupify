// feature/personalbum/src/main/.../domain/repository/PersonRepository.kt
package com.example.groupify.feature.personalbum.domain.repository

import com.example.groupify.feature.personalbum.domain.model.Person
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    suspend fun save(person: Person)
    fun getById(id: String): Flow<Person?>
    fun getAll(): Flow<List<Person>>
}
