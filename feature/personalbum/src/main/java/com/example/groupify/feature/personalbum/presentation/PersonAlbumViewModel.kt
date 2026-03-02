package com.example.groupify.feature.personalbum.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.example.groupify.feature.personalbum.domain.usecase.IndexFacesAndEmbeddingsUseCase
import com.example.groupify.feature.personalbum.domain.usecase.SearchByPhotoUseCase
import com.example.groupify.feature.personalbum.presentation.model.MatchUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonAlbumViewModel @Inject constructor(
    private val indexFacesAndEmbeddingsUseCase: IndexFacesAndEmbeddingsUseCase,
    private val searchByPhotoUseCase: SearchByPhotoUseCase,
    private val faceIndexRepository: FaceIndexRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonAlbumContract.UiState())
    val uiState: StateFlow<PersonAlbumContract.UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PersonAlbumContract.UiEffect>()
    val uiEffect: SharedFlow<PersonAlbumContract.UiEffect> = _uiEffect.asSharedFlow()

    fun onEvent(event: PersonAlbumContract.UiEvent) {
        when (event) {
            is PersonAlbumContract.UiEvent.PickQueryPhoto -> onPickQueryPhoto(event.uri)
            is PersonAlbumContract.UiEvent.StartDetection -> onStartDetection()
            is PersonAlbumContract.UiEvent.ShareMatches -> onShareMatches()
            is PersonAlbumContract.UiEvent.DismissMessage -> _uiState.update { it.copy(userMessage = null) }
        }
    }

    fun onPermissionDenied() {
        _uiState.update { it.copy(userMessage = "Storage permission is required to access your photos.") }
    }

    private fun onPickQueryPhoto(uri: String) {
        _uiState.update { it.copy(selectedQueryPhotoUri = uri, matches = emptyList(), userMessage = null) }
    }

    private fun onStartDetection() {
        val queryUri = _uiState.value.selectedQueryPhotoUri ?: return
        if (_uiState.value.isPreparingGallery || _uiState.value.isDetecting) return

        viewModelScope.launch {
            try {
                // Auto-index if the face DB is empty
                val storedFaces = faceIndexRepository.getAllFaces().first()
                if (storedFaces.isEmpty()) {
                    _uiState.update {
                        it.copy(isPreparingGallery = true, preparingProgressCurrent = 0, preparingProgressTotal = 0)
                    }
                    indexFacesAndEmbeddingsUseCase().collect { progress ->
                        _uiState.update {
                            it.copy(
                                preparingProgressCurrent = progress.current,
                                preparingProgressTotal = progress.total,
                            )
                        }
                    }
                    _uiState.update { it.copy(isPreparingGallery = false) }
                }

                // Run face search
                _uiState.update { it.copy(isDetecting = true, matches = emptyList()) }
                val results = searchByPhotoUseCase(queryUri)
                val matchUiModels = results.map { match ->
                    MatchUiModel(
                        uri = match.uri,
                        scorePercent = (match.score * 100).toInt().coerceIn(0, 100),
                    )
                }
                _uiState.update { it.copy(matches = matchUiModels) }
            } catch (e: IllegalArgumentException) {
                _uiState.update { it.copy(userMessage = e.message ?: "No face detected in the selected photo.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(userMessage = e.message ?: "Detection failed. Please try again.") }
            } finally {
                _uiState.update { it.copy(isPreparingGallery = false, isDetecting = false) }
            }
        }
    }

    private fun onShareMatches() {
        viewModelScope.launch {
            val uris = _uiState.value.matches.map { it.uri }
            if (uris.isEmpty()) {
                _uiState.update { it.copy(userMessage = "No matches to share.") }
            } else {
                _uiEffect.emit(PersonAlbumContract.UiEffect.ShareUris(uris))
            }
        }
    }
}
