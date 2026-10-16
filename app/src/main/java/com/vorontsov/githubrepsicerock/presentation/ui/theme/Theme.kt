package com.vorontsov.githubrepsicerock.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val GithubDarkColorScheme = darkColorScheme(
    primary = PrimaryButton,
    onPrimary = TextPrimary,
    primaryContainer = PrimaryButton,
    onPrimaryContainer = TextPrimary,
    inversePrimary = PrimaryButton,
    secondary = Link,
    onSecondary = BackgroundPrimary,
    secondaryContainer = Link,
    onSecondaryContainer = BackgroundPrimary,
    background = BackgroundPrimary,
    onBackground = TextPrimary,
    surface = BackgroundPrimary,
    onSurface = TextPrimary,
    surfaceVariant = TopBarBackground,
    onSurfaceVariant = TextPrimary,
    outline = Divider,
    outlineVariant = Divider,
    error = Error,
    onError = TextPrimary,
    errorContainer = Error,
    onErrorContainer = TextPrimary,
    inverseSurface = BackgroundPrimary,
    inverseOnSurface = TextPrimary
)

@Immutable
data class ExtendedColors(
    val stars: Color,
    val forks: Color,
    val watchers: Color
)

private val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        stars = StarsAccent,
        forks = ForksAccent,
        watchers = WatchersAccent
    )
}

val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalExtendedColors.current

@Composable
fun GitHubRepsIceRockTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalExtendedColors provides ExtendedColors(
            stars = StarsAccent,
            forks = ForksAccent,
            watchers = WatchersAccent
        )
    ) {
        MaterialTheme(
            colorScheme = GithubDarkColorScheme,
            typography = Typography,
            content = content
        )
    }
}
