package com.example.mlkit.feature.explorer.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.mlkit.feature.explorer.presentation.ExplorerRoute
import com.example.mlkit.core.model.PMLKitFeature

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