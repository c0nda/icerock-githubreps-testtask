package com.vorontsov.githubrepsicerock.data.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class GitHubUserDto(
    val login: String
)