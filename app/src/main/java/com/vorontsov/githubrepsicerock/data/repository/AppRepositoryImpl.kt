package com.vorontsov.githubrepsicerock.data.repository

import com.vorontsov.githubrepsicerock.data.api.GitHubApi
import com.vorontsov.githubrepsicerock.data.api.UnauthorizedException
import com.vorontsov.githubrepsicerock.data.storage.KeyValueStorage
import com.vorontsov.githubrepsicerock.data.toDomain
import com.vorontsov.githubrepsicerock.domain.model.Repository
import com.vorontsov.githubrepsicerock.domain.model.RepositoryDetails
import com.vorontsov.githubrepsicerock.domain.repository.AppRepository

class AppRepositoryImpl(
    private val api: GitHubApi,
    private val storage: KeyValueStorage
) : AppRepository {

    override suspend fun getRepositories(): List<Repository> {
        val token = requireAuthToken()
        return api.getUserRepositories(token)
            .map { repository -> repository.toDomain() }
    }

    override suspend fun getRepository(repositoryId: Long): RepositoryDetails {
        val token = requireAuthToken()
        return api.getRepository(token, repositoryId).toDomain()
    }

    override suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String
    ): String? {
        val token = requireAuthToken()
        return api.getRepositoryReadme(token, ownerName, repositoryName)
    }

    override suspend fun signIn(token: String): String {
        val user = api.getUser(token)
        storage.authToken = token
        return user.login
    }

    override fun signOut() {
        storage.clearAuthToken()
    }

    private fun requireAuthToken(): String =
        storage.authToken ?: throw UnauthorizedException("Authorization is required")
}