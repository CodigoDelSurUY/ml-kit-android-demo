package com.example.mlkit.feature.objectdetection.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.mlkit.feature.objectdetection.presentation.ObjectDetectionRoute

const val OBJECT_DETECTION_ROUTE = "object_detection_route"

fun NavController.navigateToObjectDetection(navOptions: NavOptions? = null) =
    navigate(OBJECT_DETECTION_ROUTE, navOptions)

fun NavGraphBuilder.objectDetectionScreen(onBackClick: () -> Unit) {
    composable(route = OBJECT_DETECTION_ROUTE) {
        ObjectDetectionRoute(onBackClick = onBackClick)
    }
}