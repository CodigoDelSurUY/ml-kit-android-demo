package com.example.mlkit.feature.textrecognition.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.mlkit.feature.textrecognition.presentation.TextRecognitionRoute

const val TEXT_RECOGNITION_ROUTE = "text_recognition_route"

fun NavController.navigateToTextRecognition(navOptions: NavOptions? = null) =
    navigate(TEXT_RECOGNITION_ROUTE, navOptions)

fun NavGraphBuilder.textRecognitionScreen(onBackClick: () -> Unit) {
    composable(route = TEXT_RECOGNITION_ROUTE) {
        TextRecognitionRoute(onBackClick = onBackClick)
    }
}