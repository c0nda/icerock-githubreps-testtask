package com.vorontsov.githubrepsicerock.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubRepositoryDetailsDto(
    @SerialName("forks_count")
    val forksCount: Int = 0,
    @SerialName("stargazers_count")
    val starsCount: Int = 0,
    @SerialName("watchers_count")
    val watchersCount: Int = 0,
    @SerialName("html_url")
    val url: String? = null,
    val license: GitHubLicenseDto? = null
)