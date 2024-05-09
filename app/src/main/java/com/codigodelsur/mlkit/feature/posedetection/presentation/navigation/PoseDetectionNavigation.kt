package com.codigodelsur.mlkit.feature.posedetection.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.codigodelsur.mlkit.feature.posedetection.presentation.PoseDetectionRoute

const val POSE_DETECTION_ROUTE = "pose_detection_route"

fun NavController.navigateToPoseDetection(navOptions: NavOptions? = null) =
    navigate(POSE_DETECTION_ROUTE, navOptions)

fun NavGraphBuilder.poseDetectionScreen(
    onBackClick: () -> Unit
) {
    composable(route = POSE_DETECTION_ROUTE) {
        PoseDetectionRoute(
            onBackClick = onBackClick,
        )
    }
}