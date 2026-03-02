package com.example.groupify.feature.personalbum.presentation

import com.example.groupify.feature.personalbum.presentation.model.MatchUiModel

object PersonAlbumContract {

    data class UiState(
        val selectedQueryPhotoUri: String? = null,
        val isPreparingGallery: Boolean = false,
        val preparingProgressCurrent: Int = 0,
        val preparingProgressTotal: Int = 0,
        val isDetecting: Boolean = false,
        val matches: List<MatchUiModel> = emptyList(),
        val userMessage: String? = null,
    )

    sealed interface UiEvent {
        data class PickQueryPhoto(val uri: String) : UiEvent
        data object StartDetection : UiEvent
        data object ShareMatches : UiEvent
        data object DismissMessage : UiEvent
    }

    sealed interface UiEffect {
        data class ShareUris(val uris: List<String>) : UiEffect
    }
}
