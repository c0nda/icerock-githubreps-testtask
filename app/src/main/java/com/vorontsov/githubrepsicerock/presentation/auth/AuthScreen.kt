package com.vorontsov.githubrepsicerock.presentation.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vorontsov.githubrepsicerock.R
import com.vorontsov.githubrepsicerock.presentation.common.toMessageRes
import com.vorontsov.githubrepsicerock.presentation.common.toStringTypeRes
import com.vorontsov.githubrepsicerock.presentation.ui.theme.buttonText
import com.vorontsov.githubrepsicerock.presentation.ui.theme.captionText
import com.vorontsov.githubrepsicerock.presentation.ui.theme.dialogButtonText
import com.vorontsov.githubrepsicerock.presentation.ui.theme.primaryText
import com.vorontsov.githubrepsicerock.presentation.ui.theme.screenTitle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    onSignedIn: () -> Unit
) {
    val viewModel: AuthViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val token by viewModel.token.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is AuthViewModel.AuthAction.RouteToMain -> onSignedIn()
            }
        }
    }

    AuthContent(
        state = state,
        token = token,
        onTokenChanged = viewModel::onTokenChanged,
        onSignButtonPressed = viewModel::onSignButtonPressed,
        onErrorDialogDismissed = viewModel::onErrorDialogDismissed
    )
}

@Composable
private fun AuthContent(
    state: AuthViewModel.AuthState,
    token: String,
    onTokenChanged: (String) -> Unit,
    onSignButtonPressed: () -> Unit,
    onErrorDialogDismissed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLoading = state == AuthViewModel.AuthState.Loading
    val error = (state as? AuthViewModel.AuthState.Error)?.type
    val fieldError = error?.takeIf { it !is AuthViewModel.AuthError.Common }
    val dialogError = error as? AuthViewModel.AuthError.Common

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(80.dp))

            TextField(
                value = token,
                onValueChange = onTokenChanged,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.primaryText,
                isError = fieldError != null,
                label = {
                    Text(text = stringResource(R.string.auth_token_label))
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                ),
                supportingText = {
                    val fieldMessageRes = fieldError?.fieldMessageRes()

                    if (fieldMessageRes != null) {
                        Text(
                            text = stringResource(fieldMessageRes),
                            style = MaterialTheme.typography.captionText,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSignButtonPressed,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.auth_sign_in_button),
                        style = MaterialTheme.typography.buttonText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (dialogError != null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
            ) {
                AlertDialog(
                    onDismissRequest = onErrorDialogDismissed,
                    confirmButton = {
                        TextButton(
                            onClick = onErrorDialogDismissed
                        ) {
                            Text(
                                text = stringResource(R.string.auth_error_dialog_ok),
                                style = MaterialTheme.typography.dialogButtonText,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(R.string.auth_error_dialog_title),
                            style = MaterialTheme.typography.screenTitle
                        )
                    },
                    text = {
                        val view = LocalView.current

                        SideEffect {
                            (view.parent as? DialogWindowProvider)?.window?.setDimAmount(0f)
                        }

                        Column {
                            Text(
                                text = stringResource(dialogError.type.toStringTypeRes()),
                                style = MaterialTheme.typography.primaryText
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = stringResource(dialogError.type.toMessageRes()),
                                style = MaterialTheme.typography.primaryText
                            )
                        }
                    },
                    shape = RectangleShape,
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    textContentColor = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@StringRes
private fun AuthViewModel.AuthError.fieldMessageRes(): Int? = when (this) {
    AuthViewModel.AuthError.EmptyToken -> R.string.auth_error_empty_token
    AuthViewModel.AuthError.InvalidToken -> R.string.auth_error_invalid_token
    is AuthViewModel.AuthError.Common -> null
}