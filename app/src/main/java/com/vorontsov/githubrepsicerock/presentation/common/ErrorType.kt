package com.vorontsov.githubrepsicerock.presentation.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.vorontsov.githubrepsicerock.R
import com.vorontsov.githubrepsicerock.data.api.NetworkException
import com.vorontsov.githubrepsicerock.data.api.NotFoundException

enum class ErrorType {
    Network,
    NotFound,
    Server
}

fun Throwable.toErrorType(): ErrorType = when (this) {
    is NetworkException -> ErrorType.Network
    is NotFoundException -> ErrorType.NotFound
    else -> ErrorType.Server
}

@StringRes
fun ErrorType.toStringTypeRes(): Int = when (this) {
    ErrorType.Network -> R.string.connection_error
    ErrorType.NotFound -> R.string.not_found_error
    ErrorType.Server -> R.string.server_error
}

@StringRes
fun ErrorType.toMessageRes(): Int = when (this) {
    ErrorType.Network -> R.string.check_connection
    ErrorType.NotFound -> R.string.not_found_error_message
    ErrorType.Server -> R.string.try_later
}

@DrawableRes
fun ErrorType.toDrawableRes(): Int = when (this) {
    ErrorType.Network -> R.drawable.connection_error
    ErrorType.NotFound -> R.drawable.warning
    ErrorType.Server -> R.drawable.warning
}