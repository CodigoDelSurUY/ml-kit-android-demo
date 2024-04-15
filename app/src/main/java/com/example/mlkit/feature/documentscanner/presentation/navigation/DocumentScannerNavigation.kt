package com.example.mlkit.feature.documentscanner.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.mlkit.feature.documentscanner.presentation.DocumentScannerRoute

const val DOCUMENT_SCANNER_ROUTE = "document_scanner_route"

fun NavController.navigateToDocumentScanner(navOptions: NavOptions? = null) =
    navigate(DOCUMENT_SCANNER_ROUTE, navOptions)

fun NavGraphBuilder.documentScannerScreen(onBackClick: () -> Unit) {
    composable(route = DOCUMENT_SCANNER_ROUTE) {
        DocumentScannerRoute(onBackClick = onBackClick)
    }
}