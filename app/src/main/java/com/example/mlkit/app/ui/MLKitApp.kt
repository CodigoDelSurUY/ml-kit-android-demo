package com.example.mlkit.app.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mlkit.app.ui.navigation.MLKitNavHost
import com.example.mlkit.app.ui.theme.MLKitTheme

@Composable
fun MLKitApp(
    appState: MLKitAppState = rememberMLKitAppState(),
) {
    MLKitTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0.dp),
        ) { contentPadding ->
            MLKitNavHost(
                modifier = Modifier.padding(contentPadding),
                navController = appState.navController
            )
        }
    }
}