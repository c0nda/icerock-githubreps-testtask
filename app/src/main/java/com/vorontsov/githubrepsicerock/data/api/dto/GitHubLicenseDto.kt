package com.vorontsov.githubrepsicerock.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubLicenseDto(
    @SerialName("spdx_id")
    val name: String? = null
)