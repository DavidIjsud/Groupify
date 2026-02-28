// feature/personalbum/src/main/.../presentation/PersonAlbumContract.kt
package com.example.groupify.feature.personalbum.presentation

import com.example.groupify.feature.personalbum.presentation.model.PersonUiModel

object PersonAlbumContract {

    data class UiState(
        val isIndexing: Boolean = false,
        val indexedCount: Int = 0,
        val faceCount: Int = 0,
        val isCreatingPerson: Boolean = false,
        val isMatching: Boolean = false,
        val matchCount: Int = 0,
        val referencePhotoUri: String? = null,
        val persons: List<PersonUiModel> = emptyList(),
        val selectedPersonId: String? = null,
        val isLoadingAlbum: Boolean = false,
        val albumUris: List<String> = emptyList(),
        val error: String? = null,
    )

    sealed interface UiEvent {
        data object StartIndexing : UiEvent
        data object TestFaceDetection : UiEvent
        data object UseLatestPhotoAsReference : UiEvent
        data class FindMatches(val referencePhotoUri: String) : UiEvent
        data class CreatePerson(val name: String, val referencePhotoUri: String) : UiEvent
        data class SelectPerson(val personId: String) : UiEvent
        data class LoadAlbum(val personId: String) : UiEvent
    }

    sealed interface UiEffect {
        data class NavigateToAlbum(val personId: String) : UiEffect
        data class ShowError(val message: String) : UiEffect
    }
}
