package com.vorontsov.githubrepsicerock.domain.repository

import com.vorontsov.githubrepsicerock.domain.model.Repository
import com.vorontsov.githubrepsicerock.domain.model.RepositoryDetails

interface AppRepository {

    suspend fun getRepositories(): List<Repository>

    suspend fun getRepository(repositoryId: Long): RepositoryDetails

    suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String
    ): String?

    suspend fun signIn(token: String): String

    fun signOut()
}