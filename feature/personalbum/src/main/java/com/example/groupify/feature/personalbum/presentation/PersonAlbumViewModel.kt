// feature/personalbum/src/main/.../presentation/PersonAlbumViewModel.kt
package com.example.groupify.feature.personalbum.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupify.feature.personalbum.domain.model.Person
import com.example.groupify.feature.personalbum.domain.usecase.CreatePersonAlbumUseCase
import com.example.groupify.feature.personalbum.domain.usecase.IndexPhotosUseCase
import com.example.groupify.feature.personalbum.presentation.model.PersonUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonAlbumViewModel @Inject constructor(
    private val indexPhotosUseCase: IndexPhotosUseCase,
    private val createPersonAlbumUseCase: CreatePersonAlbumUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonAlbumContract.UiState())
    val uiState: StateFlow<PersonAlbumContract.UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PersonAlbumContract.UiEffect>()
    val uiEffect: SharedFlow<PersonAlbumContract.UiEffect> = _uiEffect.asSharedFlow()

    fun onEvent(event: PersonAlbumContract.UiEvent) {
        when (event) {
            is PersonAlbumContract.UiEvent.StartIndexing -> onStartIndexing()
            is PersonAlbumContract.UiEvent.CreatePerson -> onCreatePerson(event.name, event.referencePhotoUri)
            is PersonAlbumContract.UiEvent.SelectPerson -> onSelectPerson(event.personId)
        }
    }

    private fun onStartIndexing() {
        viewModelScope.launch {
            TODO("Collect indexPhotosUseCase(), update uiState.isIndexing / indexedCount")
        }
    }

    private fun onCreatePerson(name: String, referencePhotoUri: String) {
        viewModelScope.launch {
            TODO("Resolve referencePhotoUri to embedding, call createPersonAlbumUseCase, emit NavigateToAlbum effect")
        }
    }

    private fun onSelectPerson(personId: String) {
        viewModelScope.launch {
            _uiEffect.emit(PersonAlbumContract.UiEffect.NavigateToAlbum(personId))
        }
    }

    private fun Person.toUiModel(): PersonUiModel = PersonUiModel(id = id, name = name)
}
