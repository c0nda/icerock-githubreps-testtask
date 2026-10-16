package com.vorontsov.githubrepsicerock.presentation.details

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vorontsov.githubrepsicerock.R
import com.vorontsov.githubrepsicerock.presentation.common.ErrorContent
import com.vorontsov.githubrepsicerock.presentation.ui.theme.infoText
import com.vorontsov.githubrepsicerock.presentation.ui.theme.screenTitle
import org.koin.compose.viewmodel.koinViewModel
import androidx.core.net.toUri
import com.vorontsov.githubrepsicerock.domain.model.RepositoryDetails
import com.vorontsov.githubrepsicerock.presentation.common.LoadingContent
import com.vorontsov.githubrepsicerock.presentation.common.MarkdownContent
import com.vorontsov.githubrepsicerock.presentation.common.toDrawableRes
import com.vorontsov.githubrepsicerock.presentation.common.toMessageRes
import com.vorontsov.githubrepsicerock.presentation.common.toStringTypeRes
import com.vorontsov.githubrepsicerock.presentation.ui.theme.extendedColors

@Composable
fun DetailInfoScreen(
    onBackButtonPressed: () -> Unit
) {
    val viewModel: RepositoryInfoViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailInfoContent(
        state = state,
        repositoryName = viewModel.repositoryName,
        onBackButtonPressed = onBackButtonPressed,
        onRetryRepositoryButtonPressed = viewModel::onRetryRepositoryButtonPressed,
        onRetryReadmeButtonPressed = viewModel::onRetryReadmeButtonPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailInfoContent(
    state: RepositoryInfoViewModel.RepositoryInfoState,
    repositoryName: String,
    onBackButtonPressed: () -> Unit,
    onRetryRepositoryButtonPressed: () -> Unit,
    onRetryReadmeButtonPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = repositoryName,
                        style = MaterialTheme.typography.screenTitle,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackButtonPressed) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.back),
                            contentDescription = stringResource(R.string.back_content_description),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { innerPadding ->
        when (val repositoryState = state.repositoryState) {
            RepositoryInfoViewModel.RepositoryState.Loading -> {
                LoadingContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                )
            }

            is RepositoryInfoViewModel.RepositoryState.Error -> {
                ErrorContent(
                    errorType = stringResource(repositoryState.type.toStringTypeRes()),
                    errorMessage = stringResource(repositoryState.type.toMessageRes()),
                    onRetryButtonPressed = onRetryRepositoryButtonPressed,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    icon = painterResource(repositoryState.type.toDrawableRes())
                )
            }

            is RepositoryInfoViewModel.RepositoryState.Loaded -> {
                RepositoryContent(
                    repository = repositoryState.repository,
                    readmeState = state.readmeState,
                    onRetryReadmeButtonPressed = onRetryReadmeButtonPressed,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun RepositoryContent(
    repository: RepositoryDetails,
    readmeState: RepositoryInfoViewModel.ReadmeState,
    onRetryReadmeButtonPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (readmeState is RepositoryInfoViewModel.ReadmeState.Error) {
        Column(
            modifier = modifier
        ) {
            RepositoryInfoSection(repository = repository)

            ErrorContent(
                errorType = stringResource(readmeState.type.toStringTypeRes()),
                errorMessage = stringResource(readmeState.type.toMessageRes()),
                onRetryButtonPressed = onRetryReadmeButtonPressed,
                modifier = Modifier.weight(1f),
                icon = painterResource(readmeState.type.toDrawableRes())
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
        ) {
            item {
                RepositoryInfoSection(repository = repository)
            }

            item {
                ReadmeContent(
                    readmeState = readmeState,
                    onRetryReadmeButtonPressed = onRetryReadmeButtonPressed
                )
            }
        }
    }
}

@Composable
private fun RepositoryInfoSection(
    repository: RepositoryDetails,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        repository.url?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            openInGithub(context, it)
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.link),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = it.removePrefix("https://").removePrefix("http://"),
                    style = MaterialTheme.typography.infoText,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        repository.license?.let { license ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.license),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = stringResource(R.string.details_license),
                    style = MaterialTheme.typography.infoText,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = license,
                    style = MaterialTheme.typography.infoText,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RepositoryStat(
                icon = painterResource(R.drawable.star),
                count = repository.starsCount,
                subject = stringResource(R.string.repository_stars),
                tint = MaterialTheme.extendedColors.stars,
                contentDescription = stringResource(
                    R.string.repository_star_count_desc,
                    repository.starsCount
                )
            )

            RepositoryStat(
                icon = painterResource(R.drawable.fork),
                count = repository.forksCount,
                subject = stringResource(R.string.repository_forks),
                    tint = MaterialTheme.extendedColors.forks,
                contentDescription = stringResource(
                    R.string.repository_fork_count_desc,
                    repository.forksCount
                )
            )

            RepositoryStat(
                icon = painterResource(R.drawable.view),
                count = repository.watchersCount,
                subject = stringResource(R.string.repository_watchers),
                    tint = MaterialTheme.extendedColors.watchers,
                contentDescription = stringResource(
                    R.string.repository_watcher_count_desc,
                    repository.watchersCount
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun RepositoryStat(
    icon: Painter,
    count: Int,
    subject: String,
    tint: Color,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = count.toString(),
            style = MaterialTheme.typography.infoText,
            color = tint
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = subject,
            style = MaterialTheme.typography.infoText,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun ReadmeContent(
    readmeState: RepositoryInfoViewModel.ReadmeState,
    onRetryReadmeButtonPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (readmeState) {
        RepositoryInfoViewModel.ReadmeState.Loading -> {
            LoadingContent(
                modifier = modifier,
                progressSize = 24.dp
            )
        }

        RepositoryInfoViewModel.ReadmeState.Empty -> {
            Text(
                text = stringResource(R.string.details_readme_empty),
                style = MaterialTheme.typography.infoText,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = modifier
                    .padding(start = 8.dp)
                    .alpha(0.7f)
            )
        }

        is RepositoryInfoViewModel.ReadmeState.Error -> {
            ErrorContent(
                errorType = stringResource(readmeState.type.toStringTypeRes()),
                errorMessage = stringResource(readmeState.type.toMessageRes()),
                onRetryButtonPressed = onRetryReadmeButtonPressed,
                modifier = Modifier
                    .fillMaxSize(),
                icon = painterResource(readmeState.type.toDrawableRes())
            )
        }

        is RepositoryInfoViewModel.ReadmeState.Loaded -> {
            Surface(
                modifier = modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface
            ) {
                MarkdownContent(
                    markdown = readmeState.markdown
                )
            }
        }
    }
}

private fun openInGithub(context: Context, url: String?) {
    if (url.isNullOrBlank()) return

    val intent = Intent(Intent.ACTION_VIEW, url.toUri())

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}