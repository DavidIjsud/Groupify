// feature/personalbum/src/main/java/com/example/groupify/feature/personalbum/presentation/PersonAlbumViewModel.kt
package com.example.groupify.feature.personalbum.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupify.feature.personalbum.domain.usecase.IndexFacesAndEmbeddingsUseCase
import com.example.groupify.feature.personalbum.domain.usecase.SearchByPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonAlbumViewModel @Inject constructor(
    private val indexFacesAndEmbeddingsUseCase: IndexFacesAndEmbeddingsUseCase,
    private val searchByPhotoUseCase: SearchByPhotoUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonAlbumContract.UiState())
    val uiState: StateFlow<PersonAlbumContract.UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PersonAlbumContract.UiEffect>()
    val uiEffect: SharedFlow<PersonAlbumContract.UiEffect> = _uiEffect.asSharedFlow()

    fun onEvent(event: PersonAlbumContract.UiEvent) {
        when (event) {
            is PersonAlbumContract.UiEvent.StartIndexing -> onStartIndexing()
            is PersonAlbumContract.UiEvent.PickQueryPhoto -> onPickQueryPhoto(event.uri)
            is PersonAlbumContract.UiEvent.StartSearch -> onStartSearch()
            is PersonAlbumContract.UiEvent.ShareMatches -> onShareMatches()
        }
    }

    fun onPermissionDenied() {
        viewModelScope.launch {
            _uiEffect.emit(PersonAlbumContract.UiEffect.ShowError("Permission required"))
        }
    }

    private fun onStartIndexing() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isIndexing = true, indexedCount = 0) }
                indexFacesAndEmbeddingsUseCase().collect { progress ->
                    _uiState.update { it.copy(indexedCount = progress.current) }
                }
            } catch (e: Exception) {
                _uiEffect.emit(
                    PersonAlbumContract.UiEffect.ShowError(e.message ?: "Indexing failed")
                )
            } finally {
                _uiState.update { it.copy(isIndexing = false) }
            }
        }
    }

    private fun onPickQueryPhoto(uri: String) {
        _uiState.update { it.copy(queryPhotoUri = uri, matchUris = emptyList(), error = null) }
    }

    private fun onStartSearch() {
        val queryUri = _uiState.value.queryPhotoUri ?: return
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSearching = true, matchUris = emptyList()) }
                val results = searchByPhotoUseCase(queryUri)
                _uiState.update { it.copy(matchUris = results) }
            } catch (e: Exception) {
                _uiEffect.emit(
                    PersonAlbumContract.UiEffect.ShowError(e.message ?: "Search failed")
                )
            } finally {
                _uiState.update { it.copy(isSearching = false) }
            }
        }
    }

    private fun onShareMatches() {
        viewModelScope.launch {
            val uris = _uiState.value.matchUris
            if (uris.isEmpty()) {
                _uiEffect.emit(PersonAlbumContract.UiEffect.ShowError("No matches to share"))
            } else {
                _uiEffect.emit(PersonAlbumContract.UiEffect.ShareUris(uris))
            }
        }
    }
}
