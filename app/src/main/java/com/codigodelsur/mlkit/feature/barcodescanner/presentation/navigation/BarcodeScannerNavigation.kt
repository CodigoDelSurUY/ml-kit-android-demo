package com.codigodelsur.mlkit.feature.barcodescanner.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.codigodelsur.mlkit.feature.barcodescanner.presentation.BarcodeScannerRoute

const val BARCODE_SCANNER_ROUTE = "barcode_scanner_route"

fun NavController.navigateToBarcodeScanner(navOptions: NavOptions? = null) =
    navigate(BARCODE_SCANNER_ROUTE, navOptions)

fun NavGraphBuilder.barcodeScannerScreen(onBackClick: () -> Unit) {
    composable(route = BARCODE_SCANNER_ROUTE) {
        BarcodeScannerRoute(onBackClick = onBackClick)
    }
}