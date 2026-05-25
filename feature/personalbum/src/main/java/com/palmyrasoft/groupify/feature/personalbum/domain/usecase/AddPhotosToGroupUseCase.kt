// feature/personalbum/src/main/.../domain/usecase/AddPhotosToGroupUseCase.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.usecase

import com.palmyrasoft.groupify.feature.personalbum.domain.repository.GroupRepository
import javax.inject.Inject

class AddPhotosToGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
) {
    suspend operator fun invoke(groupId: String, photoUris: List<String>, faceCount: Int) {
        require(photoUris.isNotEmpty()) { "No photos to add" }
        groupRepository.addToGroup(groupId, photoUris, faceCount)
    }
}
