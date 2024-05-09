package com.codigodelsur.mlkit.app.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codigodelsur.mlkit.app.ui.navigation.MlkNavHost
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme
import com.codigodelsur.mlkit.core.presentation.component.LocalSnackBarHostState

@Composable
fun MLKitApp(
    appState: MlkAppState = rememberMlkAppState(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    MlkTheme {
        CompositionLocalProvider(
            LocalSnackBarHostState provides snackbarHostState
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets(0.dp),
                topBar = {

                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            ) { contentPadding ->
                MlkNavHost(
                    modifier = Modifier.padding(contentPadding),
                    navController = appState.navController
                )
            }
        }
    }
}