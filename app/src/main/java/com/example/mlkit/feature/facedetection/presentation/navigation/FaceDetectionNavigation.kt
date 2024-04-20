package com.example.mlkit.feature.facedetection.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.mlkit.feature.facedetection.presentation.FaceDetectionRoute

const val FACE_DETECTION_ROUTE = "face_detection_route"

fun NavController.navigateToFaceDetection(navOptions: NavOptions? = null) =
    navigate(FACE_DETECTION_ROUTE, navOptions)

fun NavGraphBuilder.faceDetectionScreen(
    onBackClick: () -> Unit
) {
    composable(route = FACE_DETECTION_ROUTE) {
        FaceDetectionRoute(
            onBackClick = onBackClick,
        )
    }
}