package com.vorontsov.githubrepsicerock.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoutes {

    @Serializable
    data object Splash : AppRoutes

    @Serializable
    data object Auth : AppRoutes

    @Serializable
    data object Repositories : AppRoutes

    @Serializable
    data class RepositoryDetails(
        val repositoryId: Long,
        val owner: String,
        val repoName: String
    ) : AppRoutes
}
