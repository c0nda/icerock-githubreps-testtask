package com.vorontsov.githubrepsicerock.presentation.common

import android.content.Context
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class LanguageColorProvider(context: Context) {

    private val assets = context.assets

    private val colors: Map<String, Color> by lazy {
        runCatching {
            val json = assets.open(ASSETS_FILE_NAME).bufferedReader().use { it.readText() }
            json.decodeLanguageColors()
        }.getOrDefault(emptyMap())
    }

    fun colorOf(language: String?): Color? = language?.let { colors[it] }

    private fun String.decodeLanguageColors(): Map<String, Color> =
        json.decodeFromString<Map<String, LanguageColorDto>>(this)
            .mapNotNull { (language, value) ->
                value.color?.toColorOrNull()?.let { language to it }
            }.toMap()

    @Serializable
    private data class LanguageColorDto(val color: String? = null)

    private companion object {
        const val ASSETS_FILE_NAME = "colors.json"

        val json = Json { ignoreUnknownKeys = true }
    }
}

private fun String.toColorOrNull(): Color? {
    val hex = removePrefix("#")
    if (hex.length != HEX_LENGTH) return null
    return runCatching { Color(0xFF000000L or hex.toLong(radix = 16)) }.getOrNull()
}

private const val HEX_LENGTH = 6
