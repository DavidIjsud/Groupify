package com.example.groupify.feature.personalbum.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.groupify.feature.personalbum.domain.repository.FaceIndexRepository
import com.example.groupify.feature.personalbum.domain.usecase.BuildQueryFaceThumbnailsUseCase
import com.example.groupify.feature.personalbum.domain.usecase.DetectQueryFacesUseCase
import com.example.groupify.feature.personalbum.domain.usecase.SearchByPhotoUseCase
import com.example.groupify.feature.personalbum.presentation.model.MatchUiModel
import com.example.groupify.feature.personalbum.presentation.model.QueryFaceUiModel
import com.example.groupify.feature.personalbum.workers.IndexFacesWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonAlbumViewModel @Inject constructor(
    private val workManager: WorkManager,
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
                // Auto-index if the face DB is empty.
                // The worker runs as a foreground service and survives the app going to background.
                val storedFaces = faceIndexRepository.getAllFaces().first()
                if (storedFaces.isEmpty()) {
                    _uiState.update {
                        it.copy(isPreparingGallery = true, preparingProgressCurrent = 0, preparingProgressTotal = 0)
                    }

                    val succeeded = awaitIndexing()

                    _uiState.update { it.copy(isPreparingGallery = false) }
                    if (!succeeded) {
                        _uiState.update { it.copy(userMessage = "Photo indexing failed. Please try again.") }
                        return@launch
                    }
                }

                // Run face search with selected bounding boxes and sensitivity threshold.
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

    /**
     * Enqueues [IndexFacesWorker] (with KEEP so a running worker is never duplicated) and
     * suspends until the work reaches a terminal state (SUCCEEDED, FAILED, or CANCELLED).
     *
     * While waiting, progress from the worker's [WorkInfo] is forwarded to [UiState] so the
     * UI can show a live progress bar — even if the user leaves and returns to the screen,
     * because we observe by the unique work name (not a request ID).
     *
     * Returns `true` on success, `false` on failure/cancellation.
     */
    private suspend fun awaitIndexing(): Boolean {
        val request = OneTimeWorkRequestBuilder<IndexFacesWorker>().build()
        workManager.enqueueUniqueWork(
            IndexFacesWorker.WORK_NAME,
            ExistingWorkPolicy.KEEP,
            request,
        )

        var succeeded = false

        // getWorkInfosForUniqueWorkFlow emits a new List<WorkInfo> on every state change.
        // transformWhile emits each value AND stops collection once the work is finished,
        // including emitting the terminal state so we can read the final result.
        workManager.getWorkInfosForUniqueWorkFlow(IndexFacesWorker.WORK_NAME)
            .map { list -> list.firstOrNull() }
            .transformWhile { workInfo ->
                emit(workInfo)
                workInfo?.state?.isFinished != true  // keep collecting until finished
            }
            .collect { workInfo ->
                if (workInfo == null) return@collect

                // Forward progress to UiState on every RUNNING update.
                val current = workInfo.progress.getInt(IndexFacesWorker.KEY_PROCESSED, 0)
                val total = workInfo.progress.getInt(IndexFacesWorker.KEY_TOTAL, 0)
                if (current > 0 || total > 0) {
                    _uiState.update {
                        it.copy(preparingProgressCurrent = current, preparingProgressTotal = total)
                    }
                }

                if (workInfo.state == WorkInfo.State.SUCCEEDED) succeeded = true
            }

        return succeeded
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
