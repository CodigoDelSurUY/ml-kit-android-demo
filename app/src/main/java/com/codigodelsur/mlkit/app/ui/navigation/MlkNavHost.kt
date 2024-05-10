package com.codigodelsur.mlkit.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.codigodelsur.mlkit.core.presentation.model.PMLKitFeature
import com.codigodelsur.mlkit.feature.barcodescanner.presentation.navigation.barcodeScannerScreen
import com.codigodelsur.mlkit.feature.barcodescanner.presentation.navigation.navigateToBarcodeScanner
import com.codigodelsur.mlkit.feature.documentscanner.presentation.navigation.documentScannerScreen
import com.codigodelsur.mlkit.feature.documentscanner.presentation.navigation.navigateToDocumentScanner
import com.codigodelsur.mlkit.feature.entityextraction.presentation.navigation.entityExtractionScreen
import com.codigodelsur.mlkit.feature.entityextraction.presentation.navigation.navigateToEntityExtraction
import com.codigodelsur.mlkit.feature.explorer.presentation.navigation.EXPLORER_ROUTE
import com.codigodelsur.mlkit.feature.explorer.presentation.navigation.explorerScreen
import com.codigodelsur.mlkit.feature.facedetection.presentation.navigation.faceDetectionScreen
import com.codigodelsur.mlkit.feature.facedetection.presentation.navigation.navigateToFaceDetection
import com.codigodelsur.mlkit.feature.objectdetection.presentation.navigation.navigateToObjectDetection
import com.codigodelsur.mlkit.feature.objectdetection.presentation.navigation.objectDetectionScreen
import com.codigodelsur.mlkit.feature.posedetection.presentation.navigation.navigateToPoseDetection
import com.codigodelsur.mlkit.feature.posedetection.presentation.navigation.poseDetectionScreen
import com.codigodelsur.mlkit.feature.selfiesegmentation.presentation.navigation.navigateToSelfieSegmentation
import com.codigodelsur.mlkit.feature.selfiesegmentation.presentation.navigation.selfieSegmentationScreen
import com.codigodelsur.mlkit.feature.smartreply.presentation.navigation.navigateToSmartReply
import com.codigodelsur.mlkit.feature.smartreply.presentation.navigation.smartReplyScreen
import com.codigodelsur.mlkit.feature.textrecognition.presentation.navigation.navigateToTextRecognition
import com.codigodelsur.mlkit.feature.textrecognition.presentation.navigation.textRecognitionScreen
import com.codigodelsur.mlkit.feature.translation.presentation.navigation.navigateToTranslation
import com.codigodelsur.mlkit.feature.translation.presentation.navigation.translationScreen

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
                is PMLKitFeature.Translation -> navController.navigateToTranslation()
                is PMLKitFeature.SmartReply -> navController.navigateToSmartReply()
                is PMLKitFeature.EntityExtraction -> navController.navigateToEntityExtraction()
            }
        })

        documentScannerScreen { navController.popBackStack() }
        textRecognitionScreen { navController.popBackStack() }
        faceDetectionScreen { navController.popBackStack() }
        objectDetectionScreen { navController.popBackStack() }
        barcodeScannerScreen { navController.popBackStack() }
        poseDetectionScreen { navController.popBackStack() }
        selfieSegmentationScreen { navController.popBackStack() }
        translationScreen { navController.popBackStack() }
        smartReplyScreen { navController.popBackStack() }
        entityExtractionScreen { navController.popBackStack() }
    }
}