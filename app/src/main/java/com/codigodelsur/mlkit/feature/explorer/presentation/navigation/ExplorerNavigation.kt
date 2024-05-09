package com.codigodelsur.mlkit.feature.explorer.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.codigodelsur.mlkit.feature.explorer.presentation.ExplorerRoute
import com.codigodelsur.mlkit.core.presentation.model.PMLKitFeature

const val EXPLORER_ROUTE = "explorer_route"

fun NavGraphBuilder.explorerScreen(
    onFeatureClick: (PMLKitFeature) -> Unit
) {
    composable(route = EXPLORER_ROUTE) {
        ExplorerRoute(
            onFeatureClick = onFeatureClick,
        )
    }
}