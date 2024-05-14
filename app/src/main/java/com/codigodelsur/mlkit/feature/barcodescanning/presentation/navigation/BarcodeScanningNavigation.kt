package com.codigodelsur.mlkit.feature.barcodescanning.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.codigodelsur.mlkit.feature.barcodescanning.presentation.BarcodeScanningRoute

const val BARCODE_SCANNING_ROUTE = "barcode_scanning_route"

fun NavController.navigateToBarcodeScanning(navOptions: NavOptions? = null) =
    navigate(BARCODE_SCANNING_ROUTE, navOptions)

fun NavGraphBuilder.barcodeScanningScreen(onBackClick: () -> Unit) {
    composable(route = BARCODE_SCANNING_ROUTE) {
        BarcodeScanningRoute(onBackClick = onBackClick)
    }
}