package com.vorontsov.githubrepsicerock.presentation.repositories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vorontsov.githubrepsicerock.R
import com.vorontsov.githubrepsicerock.domain.model.Repository
import com.vorontsov.githubrepsicerock.presentation.common.EmptyContent
import com.vorontsov.githubrepsicerock.presentation.common.ErrorContent
import com.vorontsov.githubrepsicerock.presentation.common.LanguageColorProvider
import com.vorontsov.githubrepsicerock.presentation.common.LoadingContent
import com.vorontsov.githubrepsicerock.presentation.common.toDrawableRes
import com.vorontsov.githubrepsicerock.presentation.common.toMessageRes
import com.vorontsov.githubrepsicerock.presentation.common.toStringTypeRes
import com.vorontsov.githubrepsicerock.presentation.ui.theme.captionText
import com.vorontsov.githubrepsicerock.presentation.ui.theme.primaryText
import com.vorontsov.githubrepsicerock.presentation.ui.theme.screenTitle
import com.vorontsov.githubrepsicerock.presentation.ui.theme.secondaryText
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RepositoriesListScreen(
    onRepositoryClick: (Repository) -> Unit,
    onSignedOut: () -> Unit
) {
    val viewModel: RepositoriesListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                RepositoriesListViewModel.RepositoriesListAction.RouteToAuth -> onSignedOut()
            }
        }
    }

    RepositoriesListContent(
        state = state,
        onRetryButtonPressed = viewModel::onRefreshButtonPressed,
        onRepositoryClick = onRepositoryClick,
        onLogoutButtonPressed = viewModel::onLogoutButtonPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RepositoriesListContent(
    state: RepositoriesListViewModel.RepositoriesListState,
    onRetryButtonPressed: () -> Unit,
    onRepositoryClick: (Repository) -> Unit,
    onLogoutButtonPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.repositories_title),
                        style = MaterialTheme.typography.screenTitle,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                actions = {
                    IconButton(onClick = onLogoutButtonPressed) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.logout),
                            contentDescription = stringResource(R.string.logout_content_description)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { innerPadding ->
        when (state) {
            RepositoriesListViewModel.RepositoriesListState.Loading -> {
                LoadingContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            is RepositoriesListViewModel.RepositoriesListState.Error -> {
                ErrorContent(
                    errorType = stringResource(state.type.toStringTypeRes()),
                    errorMessage = stringResource(state.type.toMessageRes()),
                    onRetryButtonPressed = onRetryButtonPressed,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    icon = painterResource(state.type.toDrawableRes())
                )
            }

            RepositoriesListViewModel.RepositoriesListState.Empty -> {
                EmptyContent(
                    message = stringResource(R.string.list_empty),
                    onRefreshButtonPressed = onRetryButtonPressed,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            is RepositoriesListViewModel.RepositoriesListState.Loaded -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    itemsIndexed(
                        items = state.repositories,
                        key = { _, repository -> repository.id }
                    ) { index, repository ->
                        if (index > 0) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }

                        RepositoryItem(
                            repository = repository,
                            onClick = { onRepositoryClick(repository) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RepositoryItem(
    repository: Repository,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val languageColorProvider: LanguageColorProvider = koinInject()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .clickable(
                interactionSource = null,
                onClick = onClick,
                indication = null
            ),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column {
            Row {
                Text(
                    text = repository.name,
                    style = MaterialTheme.typography.primaryText,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

                repository.language?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.captionText,
                        color = languageColorProvider.colorOf(it)
                            ?: MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            repository.description?.let { description ->
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.secondaryText,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}