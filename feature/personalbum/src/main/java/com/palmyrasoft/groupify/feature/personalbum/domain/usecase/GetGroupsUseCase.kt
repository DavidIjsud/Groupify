// feature/personalbum/src/main/.../domain/usecase/GetGroupsUseCase.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.usecase

import com.palmyrasoft.groupify.feature.personalbum.domain.model.Group
import com.palmyrasoft.groupify.feature.personalbum.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGroupsUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
) {
    operator fun invoke(): Flow<List<Group>> = groupRepository.getAll()
}
