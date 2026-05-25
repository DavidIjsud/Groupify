package com.palmyrasoft.groupify.feature.personalbum.presentation.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.palmyrasoft.groupify.feature.personalbum.domain.model.Group
import com.palmyrasoft.groupify.feature.personalbum.domain.model.normalizedGroupName
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.DeleteGroupUseCase
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.GetGroupUseCase
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.GetGroupsUseCase
import com.palmyrasoft.groupify.feature.personalbum.presentation.model.GroupDetailUiModel
import com.palmyrasoft.groupify.feature.personalbum.presentation.model.GroupUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    getGroupsUseCase: GetGroupsUseCase,
    private val getGroupUseCase: GetGroupUseCase,
    private val deleteGroupUseCase: DeleteGroupUseCase,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val listState: StateFlow<GroupsListUiState> =
        combine(getGroupsUseCase(), _query) { groups, query ->
            val all = groups.map { it.toUiModel() }
            val trimmed = query.trim()
            val filtered = if (trimmed.isEmpty()) {
                all
            } else {
                val needle = trimmed.normalizedGroupName()
                all.filter { it.name.normalizedGroupName().contains(needle) }
            }
            GroupsListUiState(
                query = query,
                hasAnyGroups = all.isNotEmpty(),
                groups = filtered,
                totalGroups = all.size,
                totalPhotos = all.sumOf { it.photoCount },
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GroupsListUiState())

    fun onSearchQueryChange(value: String) {
        _query.value = value
    }

    fun observeGroup(groupId: String): Flow<GroupDetailUiModel?> =
        getGroupUseCase(groupId).map { group ->
            group?.let {
                GroupDetailUiModel(
                    id = it.id,
                    name = it.name,
                    photoCount = it.photoCount,
                    faceCount = it.faceCount,
                    photoUris = it.photoUris,
                )
            }
        }

    fun deleteGroup(groupId: String) {
        viewModelScope.launch { deleteGroupUseCase(groupId) }
    }
}

data class GroupsListUiState(
    val query: String = "",
    val hasAnyGroups: Boolean = false,
    val groups: List<GroupUiModel> = emptyList(),
    val totalGroups: Int = 0,
    val totalPhotos: Int = 0,
)

private fun Group.toUiModel(): GroupUiModel = GroupUiModel(
    id = id,
    name = name,
    photoCount = photoCount,
    faceCount = faceCount,
    updatedAt = updatedAt,
    previewUris = photoUris.take(4),
)
