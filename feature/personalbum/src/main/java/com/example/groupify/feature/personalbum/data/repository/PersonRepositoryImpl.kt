// feature/personalbum/src/main/.../data/repository/PersonRepositoryImpl.kt
package com.example.groupify.feature.personalbum.data.repository

import com.example.groupify.feature.personalbum.domain.model.Person
import com.example.groupify.feature.personalbum.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    // TODO: inject RoomDatabase DAO once Room is integrated
) : PersonRepository {
    override suspend fun save(person: Person): Unit = TODO("Persist Person to Room")
    override fun getById(id: String): Flow<Person?> = TODO("Query Person by id from Room")
    override fun getAll(): Flow<List<Person>> = TODO("Query all Persons from Room")
}
