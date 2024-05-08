package com.example.mlkit.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.mlkit.core.presentation.model.PMLKitFeature
import com.example.mlkit.feature.barcodescanner.presentation.navigation.barcodeScannerScreen
import com.example.mlkit.feature.barcodescanner.presentation.navigation.navigateToBarcodeScanner
import com.example.mlkit.feature.documentscanner.presentation.navigation.documentScannerScreen
import com.example.mlkit.feature.documentscanner.presentation.navigation.navigateToDocumentScanner
import com.example.mlkit.feature.explorer.presentation.navigation.EXPLORER_ROUTE
import com.example.mlkit.feature.explorer.presentation.navigation.explorerScreen
import com.example.mlkit.feature.facedetection.presentation.navigation.faceDetectionScreen
import com.example.mlkit.feature.facedetection.presentation.navigation.navigateToFaceDetection
import com.example.mlkit.feature.objectdetection.presentation.navigation.navigateToObjectDetection
import com.example.mlkit.feature.objectdetection.presentation.navigation.objectDetectionScreen
import com.example.mlkit.feature.posedetection.presentation.navigation.navigateToPoseDetection
import com.example.mlkit.feature.posedetection.presentation.navigation.poseDetectionScreen
import com.example.mlkit.feature.selfiesegmentation.presentation.navigation.navigateToSelfieSegmentation
import com.example.mlkit.feature.selfiesegmentation.presentation.navigation.selfieSegmentationScreen
import com.example.mlkit.feature.textrecognition.presentation.navigation.navigateToTextRecognition
import com.example.mlkit.feature.textrecognition.presentation.navigation.textRecognitionScreen

@Composable
fun MlkNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = EXPLORER_ROUTE,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        explorerScreen(onFeatureClick = { feature ->
            when (feature) {
                is PMLKitFeature.DocumentScanner -> navController.navigateToDocumentScanner()
                is PMLKitFeature.TextRecognition -> navController.navigateToTextRecognition()
                is PMLKitFeature.BarcodeScanner -> navController.navigateToBarcodeScanner()
                is PMLKitFeature.FaceDetection -> navController.navigateToFaceDetection()
                is PMLKitFeature.SelfieSegmentation -> navController.navigateToSelfieSegmentation()
                is PMLKitFeature.ObjectDetection -> navController.navigateToObjectDetection()
                is PMLKitFeature.PoseDetection -> navController.navigateToPoseDetection()
            }
        })

        documentScannerScreen { navController.popBackStack() }
        textRecognitionScreen { navController.popBackStack() }
        faceDetectionScreen { navController.popBackStack() }
        objectDetectionScreen { navController.popBackStack() }
        barcodeScannerScreen { navController.popBackStack() }
        poseDetectionScreen { navController.popBackStack() }
        selfieSegmentationScreen { navController.popBackStack() }
    }
}