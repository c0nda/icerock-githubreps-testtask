package com.vorontsov.githubrepsicerock.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.vorontsov.githubrepsicerock.domain.model.RepositoryDetails
import com.vorontsov.githubrepsicerock.domain.repository.AppRepository
import com.vorontsov.githubrepsicerock.presentation.common.ErrorType
import com.vorontsov.githubrepsicerock.presentation.common.toErrorType
import com.vorontsov.githubrepsicerock.presentation.navigation.AppRoutes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepositoryInfoViewModel(
    private val repository: AppRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val route: AppRoutes.RepositoryDetails = savedStateHandle.toRoute()

    val repositoryName: String = route.repoName

    private val _state = MutableStateFlow(
        RepositoryInfoState(
            repositoryState = RepositoryState.Loading,
            readmeState = ReadmeState.Loading
        )
    )
    val state: StateFlow<RepositoryInfoState> = _state.asStateFlow()

    private var repositoryJob: Job? = null
    private var readmeJob: Job? = null

    init {
        loadRepository()
        loadReadme()
    }

    fun onRetryRepositoryButtonPressed() {
        loadRepository()
        loadReadme()
    }

    fun onRetryReadmeButtonPressed() {
        loadReadme()
    }

    private fun loadRepository() {
        if (repositoryJob?.isActive == true) return

        _state.value = _state.value.copy(repositoryState = RepositoryState.Loading)

        repositoryJob = viewModelScope.launch {
            try {
                val repositoryInfo = repository.getRepository(route.repositoryId)

                _state.value = _state.value.copy(
                    repositoryState = RepositoryState.Loaded(repositoryInfo)
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    repositoryState = RepositoryState.Error(e.toErrorType())
                )
            }
        }
    }

    private fun loadReadme() {
        if (readmeJob?.isActive == true) return

        _state.value = _state.value.copy(readmeState = ReadmeState.Loading)

        readmeJob = viewModelScope.launch {
            try {
                val markdown = repository.getRepositoryReadme(route.owner, route.repoName)

                _state.value = _state.value.copy(
                    readmeState = if (markdown == null) {
                        ReadmeState.Empty
                    } else {
                        ReadmeState.Loaded(markdown)
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    readmeState = ReadmeState.Error(e.toErrorType())
                )
            }
        }
    }

    sealed interface RepositoryState {
        data object Loading : RepositoryState
        data class Error(val type: ErrorType) : RepositoryState
        data class Loaded(val repository: RepositoryDetails) : RepositoryState
    }

    sealed interface ReadmeState {
        data object Loading : ReadmeState
        data object Empty : ReadmeState
        data class Error(val type: ErrorType) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }

    data class RepositoryInfoState(
        val repositoryState: RepositoryState,
        val readmeState: ReadmeState
    )
}