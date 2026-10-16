package com.vorontsov.githubrepsicerock.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    titleLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.15.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
)

val Typography.screenTitle: TextStyle
    get() = titleLarge

val Typography.infoText: TextStyle
    get() = titleMedium

val Typography.dialogButtonText: TextStyle
    get() = titleSmall

val Typography.primaryText: TextStyle
    get() = bodyLarge

val Typography.secondaryText: TextStyle
    get() = bodyMedium

val Typography.captionText: TextStyle
    get() = labelSmall

val Typography.buttonText: TextStyle
    get() = labelLarge

val MarkdownBody = TextStyle(
    fontSize = 16.sp,
    color = TextPrimary.copy(alpha = 0.7f)
)

val MarkdownHeading1 = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold,
    color = TextPrimary
)

val MarkdownHeading2 = TextStyle(
    fontSize = 22.sp,
    fontWeight = FontWeight.Bold,
    color = TextPrimary
)

val MarkdownHeading3 = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    color = TextPrimary
)

val MarkdownHeading4 = TextStyle(
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold,
    color = TextPrimary
)

val MarkdownHeading5 = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Bold,
    color = TextPrimary
)

val MarkdownHeading6 = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold,
    color = TextPrimary
)

val MarkdownCode = TextStyle(
    fontSize = 16.sp,
    color = TextPrimary.copy(alpha = 0.7f),
    background = Divider
)

val MarkdownCodeBlock = TextStyle(
    fontSize = 16.sp,
    lineHeight = 18.sp,
    fontWeight = FontWeight.Normal,
    color = TextPrimary.copy(alpha = 0.7f)
)

val MarkdownListMarker = TextStyle(
    color = TextPrimary.copy(alpha = 0.7f)
)
