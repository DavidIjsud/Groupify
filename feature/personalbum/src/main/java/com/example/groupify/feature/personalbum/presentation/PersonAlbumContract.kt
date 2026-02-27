// feature/personalbum/src/main/.../presentation/PersonAlbumContract.kt
package com.example.groupify.feature.personalbum.presentation

import com.example.groupify.feature.personalbum.domain.model.Person

object PersonAlbumContract {

    data class UiState(
        val isIndexing: Boolean = false,
        val indexedCount: Int = 0,
        val persons: List<Person> = emptyList(),
        val error: String? = null,
    )

    sealed interface UiEvent {
        data object StartIndexing : UiEvent
        data class CreatePerson(val name: String, val referenceEmbedding: FloatArray) : UiEvent
        data class SelectPerson(val personId: String) : UiEvent
    }

    sealed interface UiEffect {
        data class NavigateToAlbum(val personId: String) : UiEffect
        data class ShowError(val message: String) : UiEffect
    }
}
