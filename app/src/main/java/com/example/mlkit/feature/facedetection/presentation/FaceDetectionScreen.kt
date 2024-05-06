package com.example.mlkit.feature.facedetection.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mlkit.R
import com.example.mlkit.app.ui.theme.MlkTheme
import com.example.mlkit.app.ui.theme.Typography
import com.example.mlkit.core.presentation.component.CameraPermissionRequester
import com.example.mlkit.core.presentation.component.MlkCameraPreview
import com.example.mlkit.core.presentation.component.MlkTopAppBar
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
        showHat = state.showHat,
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
    showHat: Boolean,
    isSmiling: Boolean,
    onFacesDetected: (List<Face>) -> Unit,
    onToggleBoundingBoxes: () -> Unit,
    onToggleHat: () -> Unit,
    onBackClick: () -> Unit
) {
    val currentOnFacesDetected by rememberUpdatedState(onFacesDetected)
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
                    })

                if (showBoundingBoxes) {
                    BoundingBoxesOverlay(
                        modifier = Modifier.fillMaxSize(),
                        faces = detectedFaces
                    )
                }
                if (showHat) {
                    HatOverlay(
                        modifier = Modifier.fillMaxSize(),
                        faces = detectedFaces
                    )
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
                                id = if (showHat) {
                                    R.string.face_detection_hide_hat
                                } else {
                                    R.string.face_detection_show_hat
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

@Composable
private fun BoundingBoxesOverlay(
    modifier: Modifier,
    faces: List<Face>
) {
    Canvas(modifier = modifier.clipToBounds()) {
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

@Composable
private fun HatOverlay(
    modifier: Modifier, faces: List<Face>
) {
    val hatBitmap = ImageBitmap.imageResource(id = R.drawable.cowboy_hat)
    val yPositionAdjustment = 1.2 // Just a hardcoded adjustment based on the image padding
    Canvas(modifier = modifier.clipToBounds()) {
        for (face in faces) {
            // Draw the rectangle
            val faceBox = face.boundingBox.toComposeRect()
            val faceRect = faceBox.width
            val hatWidth = hatBitmap.width
            val scale = faceRect / hatWidth
            val hatX = faceBox.left
            val hatY = faceBox.top * yPositionAdjustment - hatBitmap.height * scale
            // Draw the hat image scaled to the width of the face
            drawImage(
                image = hatBitmap,
                srcOffset = IntOffset.Zero,
                srcSize = IntSize(hatBitmap.width, hatBitmap.height),
                dstOffset = IntOffset(hatX.toInt(), hatY.toInt()),
                dstSize = IntSize(
                    (hatBitmap.width * scale).toInt(), (hatBitmap.height * scale).toInt()
                ),
                filterQuality = FilterQuality.High
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
            showBoundingBoxes = true,
            showHat = true,
            isSmiling = true,
            onFacesDetected = { _ -> },
            onToggleBoundingBoxes = {},
            onToggleHat = {},
            onBackClick = {})
    }
}
