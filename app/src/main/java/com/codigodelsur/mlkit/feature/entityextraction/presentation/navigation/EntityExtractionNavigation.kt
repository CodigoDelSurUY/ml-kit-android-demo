package com.codigodelsur.mlkit.feature.entityextraction.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.codigodelsur.mlkit.feature.entityextraction.presentation.EntityExtractionRoute
import com.codigodelsur.mlkit.feature.smartreply.presentation.SmartReplyRoute

const val ENTITY_EXTRACTION_ROUTE = "entity_extraction_route"

fun NavController.navigateToEntityExtraction(navOptions: NavOptions? = null) =
    navigate(ENTITY_EXTRACTION_ROUTE, navOptions)

fun NavGraphBuilder.entityExtractionScreen(
    onBackClick: () -> Unit
) {
    composable(route = ENTITY_EXTRACTION_ROUTE) {
        EntityExtractionRoute(
            onBackClick = onBackClick,
        )
    }
}