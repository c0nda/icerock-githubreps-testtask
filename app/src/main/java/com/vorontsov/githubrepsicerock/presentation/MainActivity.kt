package com.vorontsov.githubrepsicerock.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.vorontsov.githubrepsicerock.presentation.navigation.AppNavHost
import com.vorontsov.githubrepsicerock.presentation.ui.theme.GitHubRepsIceRockTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubRepsIceRockTheme {
                AppNavHost(modifier = Modifier.fillMaxSize())
            }
        }
    }
}