package com.codigodelsur.mlkit.feature.facedetection.presentation

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme
import com.codigodelsur.mlkit.core.presentation.theme.Typography
import com.codigodelsur.mlkit.core.presentation.component.CameraPermissionRequester
import com.codigodelsur.mlkit.core.presentation.component.MlkCameraPreview
import com.codigodelsur.mlkit.core.presentation.component.MlkTopAppBar
import com.codigodelsur.mlkit.feature.facedetection.presentation.component.FaceBoundingBoxesOverlay
import com.codigodelsur.mlkit.feature.facedetection.presentation.component.AccessoriesOverlay
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

@Composable
fun FaceDetectionRoute(
    modifier: Modifier = Modifier,
    viewModel: FaceDetectionViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    FaceDetectionScreen(
        modifier = modifier,
        detectedFaces = state.detectedFaces,
        showBoundingBoxes = state.showBoundingBoxes,
        showAccessories = state.showHat,
        isSmiling = state.isSmiling,
        onFacesDetected = { faces ->
            viewModel.updateDetectedFaces(faces = faces)
        },
        onToggleBoundingBoxes = {
            viewModel.toggleBoundingBoxes()
        },
        onToggleHat = {
            viewModel.toggleHat()
        },
        onBackClick = onBackClick,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FaceDetectionScreen(
    modifier: Modifier,
    detectedFaces: List<Face>,
    showBoundingBoxes: Boolean,
    showAccessories: Boolean,
    isSmiling: Boolean,
    onFacesDetected: (List<Face>) -> Unit,
    onToggleBoundingBoxes: () -> Unit,
    onToggleHat: () -> Unit,
    onBackClick: () -> Unit
) {
    val currentOnFacesDetected by rememberUpdatedState(onFacesDetected)
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_FRONT_CAMERA) }
    Column(modifier = modifier.fillMaxSize()) {
        MlkTopAppBar(
            titleRes = R.string.feature_face_detection_title,
            onNavigationClick = onBackClick
        )
        CameraPermissionRequester(
            modifier = Modifier.weight(1.0f)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                MlkCameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    cameraSelector = cameraSelector,
                    onSwitchCamera = { cameraSelector = it },
                    setUpDetector = { cameraController, context ->
                        val realTimeOpts = FaceDetectorOptions.Builder()
                            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                            .enableTracking().build()

                        val faceDetector = FaceDetection.getClient(realTimeOpts)
                        val executor = ContextCompat.getMainExecutor(context)
                        cameraController.setImageAnalysisAnalyzer(
                            executor,
                            MlKitAnalyzer(
                                listOf(faceDetector),
                                ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
                                executor
                            ) { result: MlKitAnalyzer.Result? ->
                                val faces = result?.getValue(faceDetector)
                                currentOnFacesDetected(faces.orEmpty())
                            })
                    }
                ) {
                    if (showBoundingBoxes) {
                        FaceBoundingBoxesOverlay(
                            modifier = Modifier.fillMaxSize(),
                            faces = detectedFaces
                        )
                    }
                    if (showAccessories) {
                        AccessoriesOverlay(
                            modifier = Modifier.fillMaxSize(),
                            mirror = cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA,
                            faces = detectedFaces
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = onToggleBoundingBoxes) {
                        Text(
                            text = stringResource(
                                id = if (showBoundingBoxes) {
                                    R.string.face_detection_hide_bounding_boxes
                                } else {
                                    R.string.face_detection_show_bounding_boxes
                                }
                            )
                        )
                    }

                    Button(onClick = onToggleHat) {
                        Text(
                            text = stringResource(
                                id = if (showAccessories) {
                                    R.string.face_detection_hide_accessories
                                } else {
                                    R.string.face_detection_show_accessories
                                }
                            )
                        )
                    }
                }

                if (detectedFaces.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 16.dp, top = 16.dp),
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = if (isSmiling) {
                                "\uD83D\uDE0A"
                            } else {
                                "\uD83D\uDE10"
                            },
                            style = Typography.titleLarge
                        )
                    }
                }
            }
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
            showBoundingBoxes = true,
            showAccessories = true,
            isSmiling = true,
            onFacesDetected = { _ -> },
            onToggleBoundingBoxes = {},
            onToggleHat = {},
            onBackClick = {})
    }
}
