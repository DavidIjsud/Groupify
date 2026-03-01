// feature/personalbum/src/main/java/com/example/groupify/feature/personalbum/presentation/PersonAlbumContract.kt
package com.example.groupify.feature.personalbum.presentation

object PersonAlbumContract {

    data class UiState(
        val isIndexing: Boolean = false,
        val indexedCount: Int = 0,
        val queryPhotoUri: String? = null,
        val isSearching: Boolean = false,
        val matchUris: List<String> = emptyList(),
        val error: String? = null,
    )

    sealed interface UiEvent {
        data object StartIndexing : UiEvent
        data class PickQueryPhoto(val uri: String) : UiEvent
        data object StartSearch : UiEvent
        data object ShareMatches : UiEvent
    }

    sealed interface UiEffect {
        data class ShowError(val message: String) : UiEffect
        data class ShareUris(val uris: List<String>) : UiEffect
    }
}
