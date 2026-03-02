package com.example.groupify.feature.personalbum.presentation

import com.example.groupify.feature.personalbum.presentation.model.MatchUiModel
import com.example.groupify.feature.personalbum.presentation.model.QueryFaceUiModel

object PersonAlbumContract {

    data class UiState(
        val selectedQueryPhotoUri: String? = null,
        val isPreparingGallery: Boolean = false,
        val preparingProgressCurrent: Int = 0,
        val preparingProgressTotal: Int = 0,
        val isDetecting: Boolean = false,
        val matches: List<MatchUiModel> = emptyList(),
        val userMessage: String? = null,
        // Multi-face query
        val queryFaces: List<QueryFaceUiModel> = emptyList(),
        val focusedFaceId: Int? = null,
        val matchSensitivityPercent: Int = 82,
        val isFaceLoading: Boolean = false,
    )

    sealed interface UiEvent {
        data class PickQueryPhoto(val uri: String) : UiEvent
        data object StartDetection : UiEvent
        data object ShareMatches : UiEvent
        data object DismissMessage : UiEvent
        // Multi-face
        data class ToggleFaceSelection(val faceId: Int) : UiEvent
        data object SelectAllFaces : UiEvent
        data object ClearFaceSelection : UiEvent
        data class SetMatchSensitivity(val percent: Int) : UiEvent
    }

    sealed interface UiEffect {
        data class ShareUris(val uris: List<String>) : UiEffect
    }
}
