package com.vorontsov.githubrepsicerock.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vorontsov.githubrepsicerock.R
import com.vorontsov.githubrepsicerock.presentation.ui.theme.buttonText
import com.vorontsov.githubrepsicerock.presentation.ui.theme.primaryText
import com.vorontsov.githubrepsicerock.presentation.ui.theme.secondaryText

@Composable
fun LoadingContent(
    modifier: Modifier = Modifier,
    progressSize: Dp = 56.dp,
    color: Color = MaterialTheme.colorScheme.onBackground,
    strokeWidth: Dp = 4.dp
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(progressSize),
            color = color,
            strokeWidth = strokeWidth
        )
    }
}

@Composable
fun ErrorContent(
    errorType: String,
    errorMessage: String,
    onRetryButtonPressed: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.widthIn(max = 80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = errorType,
            style = MaterialTheme.typography.secondaryText,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = errorMessage,
            style = MaterialTheme.typography.secondaryText,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onRetryButtonPressed,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(36.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = stringResource(R.string.retry),
                style = MaterialTheme.typography.buttonText
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun EmptyContent(
    message: String,
    onRefreshButtonPressed: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter = painterResource(R.drawable.empty)
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.heightIn(max = 72.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.empty),
            style = MaterialTheme.typography.primaryText,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.secondaryText,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onRefreshButtonPressed,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(36.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = stringResource(R.string.refresh),
                style = MaterialTheme.typography.buttonText
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}