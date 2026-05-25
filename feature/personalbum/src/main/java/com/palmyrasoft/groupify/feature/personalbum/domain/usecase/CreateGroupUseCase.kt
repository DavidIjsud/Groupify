// feature/personalbum/src/main/.../domain/usecase/CreateGroupUseCase.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.usecase

import com.palmyrasoft.groupify.feature.personalbum.domain.model.Group
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.GroupRepository
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
) {
    suspend operator fun invoke(name: String, photoUris: List<String>, faceCount: Int): Group {
        require(name.isNotBlank()) { "Group name cannot be empty" }
        require(photoUris.isNotEmpty()) { "Cannot create an empty group" }
        return groupRepository.createGroup(name.trim(), photoUris, faceCount)
    }
}
