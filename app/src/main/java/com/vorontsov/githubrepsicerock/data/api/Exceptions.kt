package com.vorontsov.githubrepsicerock.data.api

import java.io.IOException

open class ApiException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

class UnauthorizedException(
    message: String = "Invalid authorization token",
    cause: Throwable? = null
) : ApiException(message, cause)

class NotFoundException(
    message: String,
    cause: Throwable? = null
) : ApiException(message, cause)

class NetworkException(
    message: String = "Network error",
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    companion object {
        fun from(cause: IOException): NetworkException = NetworkException(cause = cause)
    }
}