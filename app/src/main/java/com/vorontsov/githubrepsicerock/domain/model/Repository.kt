package com.vorontsov.githubrepsicerock.domain.model


data class Repository(
    val id: Long,
    val name: String,
    val isFork: Boolean,
    val owner: String,
    val language: String?,
    val description: String?
)
