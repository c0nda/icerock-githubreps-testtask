package com.vorontsov.githubrepsicerock.data.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class GithubRepositoryDto(
    val id: Long,
    val name: String,
    val isFork: Boolean = false,
    val owner: GitHubUserDto,
    val language: String? = null,
    val description: String? = null
)