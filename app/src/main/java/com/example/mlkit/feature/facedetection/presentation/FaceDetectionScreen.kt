package com.example.mlkit.feature.facedetection.presentation

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mlkit.R
import com.example.mlkit.app.ui.theme.MlkTheme
import com.example.mlkit.core.presentation.component.CameraPermissionRequester
import com.example.mlkit.core.presentation.component.MlkTopAppBar
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

@Composable
fun FaceDetectionRoute(
    modifier: Modifier = Modifier,
    faceDetectionViewModel: FaceDetectionViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by faceDetectionViewModel.state.collectAsStateWithLifecycle()
    FaceDetectionScreen(
        modifier = modifier,
        detectedFaces = state.detectedFaces,
        onFacesDetected = { faces ->
            faceDetectionViewModel.updateDetectedFaces(faces = faces)
        },
        onBackClick = onBackClick,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FaceDetectionScreen(
    modifier: Modifier,
    detectedFaces: List<Face>,
    onFacesDetected: (List<Face>) -> Unit,
    onBackClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        MlkTopAppBar(
            titleRes = R.string.feature_face_detection_title,
            onNavigationClick = onBackClick
        )
        CameraPermissionRequester(
            modifier = Modifier.weight(1.0f)
        ) {
            Box(modifier = Modifier.weight(1.0f)) {
                CameraPreview(
                    modifier = modifier.fillMaxSize(),
                    onFacesDetected = onFacesDetected
                )
                if (detectedFaces.isNotEmpty()) {
                    BoundingBoxesOverlay(
                        modifier = Modifier.fillMaxSize(),
                        faces = detectedFaces
                    )
                }
            }
        }
    }
}


@Composable
private fun CameraPreview(
    modifier: Modifier,
    onFacesDetected: (faces: List<Face>) -> Unit
) {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController =
        remember { LifecycleCameraController(context) }
    AndroidView(
        modifier = modifier,
        factory = {
            PreviewView(it).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(android.graphics.Color.BLACK)
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_START
            }.also { previewView ->
                val realTimeOpts = FaceDetectorOptions.Builder()
                    .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                    .enableTracking()
                    .build()

                val faceDetector = FaceDetection.getClient(realTimeOpts)
                cameraController.setImageAnalysisAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    MlKitAnalyzer(
                        listOf(faceDetector),
                        COORDINATE_SYSTEM_VIEW_REFERENCED,
                        ContextCompat.getMainExecutor(context)
                    ) { result: MlKitAnalyzer.Result? ->
                        val faces = result?.getValue(faceDetector)
                        onFacesDetected(faces.orEmpty())
                    }
                )
                cameraController.bindToLifecycle(lifecycleOwner)
                previewView.controller = cameraController
            }
        }
    )
}

@Composable
private fun BoundingBoxesOverlay(
    modifier: Modifier,
    faces: List<Face>
) {
    Canvas(modifier = modifier) {
        for (face in faces) {
            // Draw the rectangle
            val composeRect = face.boundingBox.toComposeRect()
            drawRect(
                color = Color.Blue,
                topLeft = composeRect.topLeft,
                size = composeRect.size,
                style = Stroke(width = 3.dp.toPx())
            )
        }
    }
}

@Preview
@Composable
private fun FaceDetectionScreenPreview() {
    MlkTheme {
        FaceDetectionScreen(
            modifier = Modifier.fillMaxSize(),
            detectedFaces = listOf(),
            onFacesDetected = { _ -> },
            onBackClick = {})
    }
}
