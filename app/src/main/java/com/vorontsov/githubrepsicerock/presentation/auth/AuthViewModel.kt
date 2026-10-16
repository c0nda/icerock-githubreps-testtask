package com.vorontsov.githubrepsicerock.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vorontsov.githubrepsicerock.data.api.UnauthorizedException
import com.vorontsov.githubrepsicerock.domain.repository.AppRepository
import com.vorontsov.githubrepsicerock.presentation.common.ErrorType
import com.vorontsov.githubrepsicerock.presentation.common.toErrorType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state = _state.asStateFlow()

    private val _actions = MutableSharedFlow<AuthAction>(extraBufferCapacity = 1)
    val actions: Flow<AuthAction> = _actions.asSharedFlow()

    val token = MutableStateFlow("")

    fun onTokenChanged(value: String) {
        token.value = value

        if (_state.value is AuthState.Error) {
            _state.value = AuthState.Idle
        }
    }

    fun onErrorDialogDismissed() {
        if (_state.value is AuthState.Error) {
            _state.value = AuthState.Idle
        }
    }

    fun onSignButtonPressed() {
        val tokenValue = token.value.trim()

        if (tokenValue.isBlank()) {
            _state.value = AuthState.Error(AuthError.EmptyToken)
            return
        }

        if (_state.value == AuthState.Loading) return

        _state.value = AuthState.Loading

        viewModelScope.launch {
            try {
                repository.signIn(tokenValue)
                _actions.tryEmit(AuthAction.RouteToMain)
            } catch (e: CancellationException) {
                throw e
            } catch (e: UnauthorizedException) {
                _state.value = AuthState.Error(AuthError.InvalidToken)
            } catch (e: Exception) {
                _state.value = AuthState.Error(AuthError.Common(e.toErrorType()))
            }
        }
    }

    sealed interface AuthState {
        data object Idle : AuthState
        data object Loading : AuthState
        data class Error(val type: AuthError) : AuthState
    }

    sealed interface AuthError {
        data object EmptyToken : AuthError
        data object InvalidToken : AuthError
        data class Common(val type: ErrorType) : AuthError
    }

    sealed interface AuthAction {
        data object RouteToMain : AuthAction
    }
}