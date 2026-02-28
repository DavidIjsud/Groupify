// feature/personalbum/src/main/.../presentation/PersonAlbumViewModel.kt
package com.example.groupify.feature.personalbum.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupify.feature.personalbum.domain.model.Person
import com.example.groupify.feature.personalbum.domain.repository.PersonRepository
import com.example.groupify.feature.personalbum.domain.repository.PhotoRepository
import com.example.groupify.feature.personalbum.domain.usecase.CreatePersonAlbumUseCase
import com.example.groupify.feature.personalbum.domain.usecase.DetectFacesInPhotoUseCase
import com.example.groupify.feature.personalbum.domain.usecase.FindMatchingPhotosUseCase
import com.example.groupify.feature.personalbum.domain.usecase.GetPersonAlbumUseCase
import com.example.groupify.feature.personalbum.domain.usecase.IndexFacesAndEmbeddingsUseCase
import com.example.groupify.feature.personalbum.presentation.model.PersonUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonAlbumViewModel @Inject constructor(
    private val indexFacesAndEmbeddingsUseCase: IndexFacesAndEmbeddingsUseCase,
    private val createPersonAlbumUseCase: CreatePersonAlbumUseCase,
    private val detectFacesInPhotoUseCase: DetectFacesInPhotoUseCase,
    private val findMatchingPhotosUseCase: FindMatchingPhotosUseCase,
    private val getPersonAlbumUseCase: GetPersonAlbumUseCase,
    private val photoRepository: PhotoRepository,
    private val personRepository: PersonRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonAlbumContract.UiState())
    val uiState: StateFlow<PersonAlbumContract.UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PersonAlbumContract.UiEffect>()
    val uiEffect: SharedFlow<PersonAlbumContract.UiEffect> = _uiEffect.asSharedFlow()

    init {
        personRepository.getAll()
            .onEach { persons ->
                _uiState.update { it.copy(persons = persons.map { p -> p.toUiModel() }) }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: PersonAlbumContract.UiEvent) {
        when (event) {
            is PersonAlbumContract.UiEvent.StartIndexing -> onStartIndexing()
            is PersonAlbumContract.UiEvent.TestFaceDetection -> onTestFaceDetection()
            is PersonAlbumContract.UiEvent.UseLatestPhotoAsReference -> onUseLatestPhotoAsReference()
            is PersonAlbumContract.UiEvent.FindMatches -> onFindMatches(event.referencePhotoUri)
            is PersonAlbumContract.UiEvent.CreatePerson -> onCreatePerson(event.name, event.referencePhotoUri)
            is PersonAlbumContract.UiEvent.SelectPerson -> onSelectPerson(event.personId)
            is PersonAlbumContract.UiEvent.LoadAlbum -> onLoadAlbum(event.personId)
            is PersonAlbumContract.UiEvent.ShareAlbum -> onShareAlbum()
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

    private fun onTestFaceDetection() {
        viewModelScope.launch {
            try {
                val photos = photoRepository.getAll().first()
                val firstPhoto = photos.firstOrNull()
                    ?: run {
                        _uiEffect.emit(PersonAlbumContract.UiEffect.ShowError("No photos available. Run indexing first."))
                        return@launch
                    }
                val faces = detectFacesInPhotoUseCase(firstPhoto.uri)
                _uiState.update { it.copy(faceCount = faces.size) }
            } catch (e: Exception) {
                _uiEffect.emit(
                    PersonAlbumContract.UiEffect.ShowError(e.message ?: "Face detection failed")
                )
            }
        }
    }

    private fun onUseLatestPhotoAsReference() {
        viewModelScope.launch {
            try {
                val firstPhoto = photoRepository.getAll().first().firstOrNull()
                    ?: run {
                        _uiEffect.emit(PersonAlbumContract.UiEffect.ShowError("No photos available."))
                        return@launch
                    }
                _uiState.update { it.copy(referencePhotoUri = firstPhoto.uri) }
            } catch (e: Exception) {
                _uiEffect.emit(
                    PersonAlbumContract.UiEffect.ShowError(e.message ?: "Failed to load photos")
                )
            }
        }
    }

    private fun onFindMatches(referencePhotoUri: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isMatching = true, matchCount = 0) }
                val result = findMatchingPhotosUseCase(referencePhotoUri)
                _uiState.update { it.copy(matchCount = result.matchCount) }
            } catch (e: Exception) {
                _uiEffect.emit(
                    PersonAlbumContract.UiEffect.ShowError(e.message ?: "Matching failed")
                )
            } finally {
                _uiState.update { it.copy(isMatching = false) }
            }
        }
    }

    private fun onCreatePerson(name: String, referencePhotoUri: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isCreatingPerson = true) }
                val person = createPersonAlbumUseCase(name, referencePhotoUri)
                _uiEffect.emit(PersonAlbumContract.UiEffect.NavigateToAlbum(person.id))
            } catch (e: Exception) {
                _uiEffect.emit(
                    PersonAlbumContract.UiEffect.ShowError(e.message ?: "Failed to create person")
                )
            } finally {
                _uiState.update { it.copy(isCreatingPerson = false) }
            }
        }
    }

    private fun onSelectPerson(personId: String) {
        onLoadAlbum(personId)
    }

    private fun onLoadAlbum(personId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(selectedPersonId = personId, isLoadingAlbum = true, albumUris = emptyList()) }
                val uris = getPersonAlbumUseCase(personId).first()
                _uiState.update { it.copy(albumUris = uris) }
            } catch (e: Exception) {
                _uiEffect.emit(
                    PersonAlbumContract.UiEffect.ShowError(e.message ?: "Failed to load album")
                )
            } finally {
                _uiState.update { it.copy(isLoadingAlbum = false) }
            }
        }
    }

    private fun onShareAlbum() {
        viewModelScope.launch {
            val uris = _uiState.value.albumUris
            if (uris.isEmpty()) {
                _uiEffect.emit(PersonAlbumContract.UiEffect.ShowError("No photos to share"))
            } else {
                _uiEffect.emit(PersonAlbumContract.UiEffect.ShareUris(uris))
            }
        }
    }

    private fun Person.toUiModel(): PersonUiModel = PersonUiModel(id = id, name = name)
}
