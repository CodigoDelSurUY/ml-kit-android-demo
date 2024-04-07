package com.example.mlkit.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.mlkit.core.model.PMLKitFeature
import com.example.mlkit.feature.documentscanner.navigation.documentScannerScreen
import com.example.mlkit.feature.documentscanner.navigation.navigateToDocumentScanner
import com.example.mlkit.feature.explorer.presentation.navigation.EXPLORER_ROUTE
import com.example.mlkit.feature.explorer.presentation.navigation.explorerScreen

@Composable
fun MLKitNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = EXPLORER_ROUTE,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        explorerScreen(onFeatureClick = { feature ->
            when(feature) {
                is PMLKitFeature.DocumentScanner -> navController.navigateToDocumentScanner()
            }
        })

        documentScannerScreen()
    }
}