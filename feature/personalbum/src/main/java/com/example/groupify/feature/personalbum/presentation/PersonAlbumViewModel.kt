package com.example.groupify.feature.personalbum.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.example.groupify.feature.personalbum.domain.usecase.BuildQueryFaceThumbnailsUseCase
import com.example.groupify.feature.personalbum.domain.usecase.DetectQueryFacesUseCase
import com.example.groupify.feature.personalbum.domain.usecase.IndexFacesAndEmbeddingsUseCase
import com.example.groupify.feature.personalbum.domain.usecase.SearchByPhotoUseCase
import com.example.groupify.feature.personalbum.presentation.model.MatchUiModel
import com.example.groupify.feature.personalbum.presentation.model.QueryFaceUiModel
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
    private val detectQueryFacesUseCase: DetectQueryFacesUseCase,
    private val buildQueryFaceThumbnailsUseCase: BuildQueryFaceThumbnailsUseCase,
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
            is PersonAlbumContract.UiEvent.ToggleFaceSelection -> onToggleFaceSelection(event.faceId)
            is PersonAlbumContract.UiEvent.SelectAllFaces -> onSelectAllFaces()
            is PersonAlbumContract.UiEvent.ClearFaceSelection -> onClearFaceSelection()
            is PersonAlbumContract.UiEvent.SetMatchSensitivity -> onSetMatchSensitivity(event.percent)
        }
    }

    fun onPermissionDenied() {
        _uiState.update { it.copy(userMessage = "Storage permission is required to access your photos.") }
    }

    private fun onPickQueryPhoto(uri: String) {
        _uiState.update {
            it.copy(
                selectedQueryPhotoUri = uri,
                matches = emptyList(),
                userMessage = null,
                queryFaces = emptyList(),
                focusedFaceId = null,
                isFaceLoading = true,
            )
        }
        detectQueryFaces(uri)
    }

    private fun detectQueryFaces(uri: String) {
        viewModelScope.launch {
            try {
                val faces = detectQueryFacesUseCase(uri)

                // Generate thumbnails; gracefully fall back to null on any failure
                val thumbnails = runCatching { buildQueryFaceThumbnailsUseCase(uri, faces) }
                    .getOrDefault(emptyMap())

                val uiModels = faces.mapIndexed { index, face ->
                    QueryFaceUiModel(
                        id = face.id,
                        label = "Face ${index + 1}",
                        boundingBox = face.boundingBox,
                        isSelected = index == 0,
                        thumbnailUri = thumbnails[face.id],
                    )
                }
                _uiState.update {
                    it.copy(
                        queryFaces = uiModels,
                        focusedFaceId = uiModels.firstOrNull()?.id,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(userMessage = e.message ?: "Could not detect faces in the selected photo.")
                }
            } finally {
                _uiState.update { it.copy(isFaceLoading = false) }
            }
        }
    }

    private fun onToggleFaceSelection(faceId: Int) {
        _uiState.update { state ->
            val updatedFaces = state.queryFaces.map { face ->
                if (face.id == faceId) face.copy(isSelected = !face.isSelected) else face
            }
            val isNowSelected = updatedFaces.firstOrNull { it.id == faceId }?.isSelected == true
            state.copy(
                queryFaces = updatedFaces,
                focusedFaceId = if (isNowSelected) faceId else state.focusedFaceId,
            )
        }
    }

    private fun onSelectAllFaces() {
        _uiState.update { state ->
            state.copy(
                queryFaces = state.queryFaces.map { it.copy(isSelected = true) },
                focusedFaceId = state.focusedFaceId ?: state.queryFaces.firstOrNull()?.id,
            )
        }
    }

    private fun onClearFaceSelection() {
        _uiState.update { state ->
            state.copy(
                queryFaces = state.queryFaces.map { it.copy(isSelected = false) },
                focusedFaceId = null,
            )
        }
    }

    private fun onSetMatchSensitivity(percent: Int) {
        _uiState.update { it.copy(matchSensitivityPercent = percent.coerceIn(60, 95)) }
    }

    private fun onStartDetection() {
        val queryUri = _uiState.value.selectedQueryPhotoUri ?: return
        val selectedFaces = _uiState.value.queryFaces.filter { it.isSelected }
        if (selectedFaces.isEmpty()) {
            _uiState.update { it.copy(userMessage = "Select at least one face to search.") }
            return
        }
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

                // Run face search with selected bounding boxes and sensitivity threshold
                _uiState.update { it.copy(isDetecting = true, matches = emptyList()) }
                val threshold = _uiState.value.matchSensitivityPercent / 100f
                val selectedBoundingBoxes = selectedFaces.map { it.boundingBox }
                val results = searchByPhotoUseCase(queryUri, selectedBoundingBoxes, threshold)
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
