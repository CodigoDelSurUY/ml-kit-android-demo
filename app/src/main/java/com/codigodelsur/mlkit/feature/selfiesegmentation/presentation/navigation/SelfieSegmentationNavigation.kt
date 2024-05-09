package com.codigodelsur.mlkit.feature.selfiesegmentation.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.codigodelsur.mlkit.feature.selfiesegmentation.presentation.SelfieSegmentationRoute

const val SELFIE_SEGMENTATION_ROUTE = "selfie_segmentation_route"

fun NavController.navigateToSelfieSegmentation(navOptions: NavOptions? = null) =
    navigate(SELFIE_SEGMENTATION_ROUTE, navOptions)

fun NavGraphBuilder.selfieSegmentationScreen(
    onBackClick: () -> Unit
) {
    composable(route = SELFIE_SEGMENTATION_ROUTE) {
        SelfieSegmentationRoute(
            onBackClick = onBackClick,
        )
    }
}