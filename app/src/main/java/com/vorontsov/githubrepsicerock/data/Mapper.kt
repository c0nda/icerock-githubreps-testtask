package com.vorontsov.githubrepsicerock.data

import com.vorontsov.githubrepsicerock.data.api.dto.GitHubRepositoryDetailsDto
import com.vorontsov.githubrepsicerock.data.api.dto.GithubRepositoryDto
import com.vorontsov.githubrepsicerock.domain.model.Repository
import com.vorontsov.githubrepsicerock.domain.model.RepositoryDetails

fun GithubRepositoryDto.toDomain(): Repository = Repository(
    id = id,
    name = name,
    isFork = isFork,
    owner = this.owner.login,
    language = language,
    description = description
)
fun GitHubRepositoryDetailsDto.toDomain(): RepositoryDetails = RepositoryDetails(
    forksCount = forksCount,
    starsCount = starsCount,
    watchersCount = watchersCount,
    url = url,
    license = this.license?.name
)
