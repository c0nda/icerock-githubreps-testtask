package com.vorontsov.githubrepsicerock.domain.model


data class RepositoryDetails(
    val forksCount: Int,
    val starsCount: Int,
    val watchersCount: Int,
    val url: String?,
    val license: String?
)