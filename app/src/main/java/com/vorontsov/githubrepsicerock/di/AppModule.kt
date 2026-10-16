package com.vorontsov.githubrepsicerock.di

import com.vorontsov.githubrepsicerock.data.HttpClientFactory
import com.vorontsov.githubrepsicerock.data.api.GitHubApi
import com.vorontsov.githubrepsicerock.data.repository.AppRepositoryImpl
import com.vorontsov.githubrepsicerock.data.storage.KeyValueStorage
import com.vorontsov.githubrepsicerock.domain.repository.AppRepository
import com.vorontsov.githubrepsicerock.domain.usecase.GetUserRepositoriesWithoutForks
import com.vorontsov.githubrepsicerock.presentation.auth.AuthViewModel
import com.vorontsov.githubrepsicerock.presentation.repositories.RepositoriesListViewModel
import com.vorontsov.githubrepsicerock.presentation.details.RepositoryInfoViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {

    single { HttpClientFactory.create() }

    singleOf(::KeyValueStorage)

    singleOf(::GitHubApi)

    singleOf(::AppRepositoryImpl) { bind<AppRepository>() }

    viewModel {
        AuthViewModel(get())
    }

    viewModel {
        RepositoriesListViewModel(get(), get())
    }

    viewModel {
        RepositoryInfoViewModel(
            savedStateHandle = get(),
            repository = get()
        )
    }

    factoryOf(::GetUserRepositoriesWithoutForks)
}