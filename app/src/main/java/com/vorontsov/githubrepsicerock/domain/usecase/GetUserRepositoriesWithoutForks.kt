package com.vorontsov.githubrepsicerock.domain.usecase

import com.vorontsov.githubrepsicerock.domain.model.Repository
import com.vorontsov.githubrepsicerock.domain.repository.AppRepository

class GetUserRepositoriesWithoutForks(
    private val appRepository: AppRepository
) {
    suspend fun execute(): List<Repository> {
        return appRepository.getRepositories()
            .filterNot { repository -> repository.isFork }
    }
}