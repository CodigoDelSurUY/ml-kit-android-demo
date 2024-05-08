package com.example.mlkit.feature.translation.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.mlkit.feature.translation.presentation.TranslationRoute

const val TRANSLATION_ROUTE = "translation_route"

fun NavController.navigateToTranslation(navOptions: NavOptions? = null) =
    navigate(TRANSLATION_ROUTE, navOptions)

fun NavGraphBuilder.translationScreen(onBackClick: () -> Unit) {
    composable(route = TRANSLATION_ROUTE) {
        TranslationRoute(onBackClick = onBackClick)
    }
}