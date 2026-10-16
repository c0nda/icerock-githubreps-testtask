package com.vorontsov.githubrepsicerock.presentation.repositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vorontsov.githubrepsicerock.domain.model.Repository
import com.vorontsov.githubrepsicerock.domain.repository.AppRepository
import com.vorontsov.githubrepsicerock.domain.usecase.GetUserRepositoriesWithoutForks
import com.vorontsov.githubrepsicerock.presentation.common.ErrorType
import com.vorontsov.githubrepsicerock.presentation.common.toErrorType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepositoriesListViewModel(
    private val repository: AppRepository,
    private val getUserRepositoriesWithoutForks: GetUserRepositoriesWithoutForks
) : ViewModel() {

    private val _state = MutableStateFlow<RepositoriesListState>(RepositoriesListState.Loading)
    val state: StateFlow<RepositoriesListState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<RepositoriesListAction>(extraBufferCapacity = 1)
    val actions: Flow<RepositoriesListAction> = _actions.asSharedFlow()

    private var loadJob: Job? = null

    init {
        loadRepositories()
    }

    fun onRefreshButtonPressed() {
        loadRepositories()
    }

    fun onLogoutButtonPressed() {
        repository.signOut()
        _actions.tryEmit(RepositoriesListAction.RouteToAuth)
    }

    private fun loadRepositories() {
        if (loadJob?.isActive == true) return
        if (_state.value is RepositoriesListState.Loaded) return

        _state.value = RepositoriesListState.Loading
        loadJob = viewModelScope.launch {
            try {
                val repositories = getUserRepositoriesWithoutForks.execute()
                _state.value = if (repositories.isEmpty()) {
                    RepositoriesListState.Empty
                } else {
                    RepositoriesListState.Loaded(repositories)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = RepositoriesListState.Error(e.toErrorType())
            }
        }
    }

    sealed interface RepositoriesListState {
        data object Loading : RepositoriesListState
        data class Loaded(val repositories: List<Repository>) : RepositoriesListState
        data object Empty : RepositoriesListState
        data class Error(val type: ErrorType) : RepositoriesListState
    }

    sealed interface RepositoriesListAction {
        data object RouteToAuth : RepositoriesListAction
    }
}