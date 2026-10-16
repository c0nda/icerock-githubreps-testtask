package com.vorontsov.githubrepsicerock.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vorontsov.githubrepsicerock.data.storage.KeyValueStorage
import com.vorontsov.githubrepsicerock.presentation.auth.AuthScreen
import com.vorontsov.githubrepsicerock.presentation.details.DetailInfoScreen
import com.vorontsov.githubrepsicerock.presentation.repositories.RepositoriesListScreen
import com.vorontsov.githubrepsicerock.presentation.splash.SplashScreen
import org.koin.compose.koinInject

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val keyValueStorage: KeyValueStorage = koinInject()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Splash,
        modifier = modifier
    ) {
        composable<AppRoutes.Splash> {
            SplashScreen(
                onFinished = {
                    val destination = if (keyValueStorage.authToken != null) {
                        AppRoutes.Repositories
                    } else {
                        AppRoutes.Auth
                    }

                    navController.navigate(destination) {
                        popUpTo(AppRoutes.Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<AppRoutes.Auth> {
            AuthScreen(
                onSignedIn = {
                    navController.navigate(AppRoutes.Repositories) {
                        popUpTo(AppRoutes.Auth) { inclusive = true }
                    }
                }
            )
        }

        composable<AppRoutes.Repositories> {
            RepositoriesListScreen(
                onRepositoryClick = { repository ->
                    navController.navigate(
                        AppRoutes.RepositoryDetails(
                            repositoryId = repository.id,
                            owner = repository.owner,
                            repoName = repository.name
                        )
                    )
                },
                onSignedOut = {
                    navController.navigate(AppRoutes.Auth) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable<AppRoutes.RepositoryDetails> {
            DetailInfoScreen(
                onBackButtonPressed = { navController.popBackStack() }
            )
        }
    }
}
