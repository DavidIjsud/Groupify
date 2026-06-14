package com.palmyrasoft.groupify.feature.personalbum.presentation

import com.palmyrasoft.groupify.feature.personalbum.presentation.model.GroupUiModel
import com.palmyrasoft.groupify.feature.personalbum.presentation.model.MatchUiModel
import com.palmyrasoft.groupify.feature.personalbum.presentation.model.QueryFaceUiModel

object PersonAlbumContract {

    /** The search modes shown by the Faces / Text / Describe segmented toggle. */
    enum class SearchMode { FACES, TEXT, DESCRIPTION }

    data class UiState(
        // Search mode toggle (Faces / Text / Describe)
        val searchMode: SearchMode = SearchMode.FACES,
        val textQuery: String = "",
        val descriptionQuery: String = "",
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
        val isFaceLoading: Boolean = false,
        // Match photo selection
        val matchSelectionMode: Boolean = false,
        val selectedMatchUris: Set<String> = emptySet(),
        // First-time indexing onboarding dialog
        val showIndexingOnboardingDialog: Boolean = false,
        // Save-to-Group flow
        val lastSearchedFaceCount: Int = 0,
        val showCreateGroupSheet: Boolean = false,
        val existingGroups: List<GroupUiModel> = emptyList(),
    ) {
        /** Photos a save/share acts on: the current selection, or all matches if none selected. */
        val actionTargetUris: List<String>
            get() = if (selectedMatchUris.isNotEmpty()) {
                matches.map { it.uri }.filter { it in selectedMatchUris }
            } else {
                matches.map { it.uri }
            }
    }

    sealed interface UiEvent {
        // Search mode
        data class SwitchMode(val mode: SearchMode) : UiEvent
        data class UpdateTextQuery(val query: String) : UiEvent
        data object RunTextSearch : UiEvent
        data class UpdateDescriptionQuery(val query: String) : UiEvent
        data object RunDescriptionSearch : UiEvent
        data class PickQueryPhoto(val uri: String) : UiEvent
        data object StartDetection : UiEvent
        data object ShareMatches : UiEvent
        data object DismissMessage : UiEvent
        // Multi-face
        data class ToggleFaceSelection(val faceId: Int) : UiEvent
        data object SelectAllFaces : UiEvent
        data object ClearFaceSelection : UiEvent
        // Match photo selection
        data object ToggleSelectionMode : UiEvent
        data class LongPressMatch(val uri: String) : UiEvent
        data class TapMatch(val uri: String) : UiEvent
        data object ClearMatchSelection : UiEvent
        data object ShareSelectedMatches : UiEvent
        data object ConfirmIndexingOnboarding : UiEvent
        // Save-to-Group flow
        data object OpenSaveToGroup : UiEvent
        data object DismissSaveToGroup : UiEvent
        data class CreateGroup(val name: String) : UiEvent
        data class AddToExistingGroup(val groupId: String) : UiEvent
    }

    sealed interface UiEffect {
        data class ShareUris(val uris: List<String>) : UiEffect
        /** A save succeeded — the screen shows a localized confirmation. */
        data class GroupSaved(val groupName: String, val isNew: Boolean) : UiEffect
    }
}
