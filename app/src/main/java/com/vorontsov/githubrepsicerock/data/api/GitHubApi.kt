package com.vorontsov.githubrepsicerock.data.api

import com.vorontsov.githubrepsicerock.data.api.dto.GitHubRepositoryDetailsDto
import com.vorontsov.githubrepsicerock.data.api.dto.GitHubUserDto
import com.vorontsov.githubrepsicerock.data.api.dto.GithubRepositoryDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import java.io.IOException

class GitHubApi(
    private val client: HttpClient
) {

    suspend fun getUser(token: String): GitHubUserDto =
        getJson {
            client.get("$BASE_URL/user") {
                bearerAuth(token)
            }
        }

    suspend fun getUserRepositories(token: String): List<GithubRepositoryDto> =
        getJson {
            client.get("$BASE_URL/user/repos") {
                bearerAuth(token)
                parameter("per_page", REPOSITORIES_PER_PAGE)
            }
        }

    suspend fun getRepository(token: String, repositoryId: Long): GitHubRepositoryDetailsDto =
        getJson {
            client.get("$BASE_URL/repositories/$repositoryId") {
                bearerAuth(token)
            }
        }

    suspend fun getRepositoryReadme(
        token: String,
        ownerName: String,
        repositoryName: String
    ): String? {
        val response = try {
            client.get("$BASE_URL/repos/$ownerName/$repositoryName/readme") {
                bearerAuth(token)
                header("Accept", README_RAW_ACCEPT)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            throw NetworkException.from(e)
        } catch (e: Exception) {
            throw NetworkException(cause = e)
        }

        return when {
            response.status == HttpStatusCode.NotFound -> null
            !response.status.isSuccess() -> throw mapError(response.status)
            else -> response.bodyAsText().takeIf { it.isNotBlank() }
        }
    }

    private suspend inline fun <reified T> getJson(
        crossinline request: suspend () -> HttpResponse
    ): T {
        val response = try {
            request()
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            throw NetworkException.from(e)
        } catch (e: Exception) {
            throw NetworkException(cause = e)
        }

        if (!response.status.isSuccess()) {
            throw mapError(response.status)
        }
        return response.body<T>()
    }

    private fun mapError(status: HttpStatusCode): Throwable = when (status) {
        HttpStatusCode.Unauthorized,
        HttpStatusCode.Forbidden -> UnauthorizedException()

        HttpStatusCode.NotFound -> NotFoundException("Requested resource was not found")

        else -> ApiException("Request failed with status ${status.value}")
    }

    private companion object {
        const val BASE_URL = "https://api.github.com"
        const val REPOSITORIES_PER_PAGE = 10
        const val README_RAW_ACCEPT = "application/vnd.github.raw"
    }
}