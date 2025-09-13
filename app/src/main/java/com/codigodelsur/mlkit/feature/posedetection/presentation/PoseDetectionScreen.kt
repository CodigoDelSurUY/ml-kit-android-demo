package com.codigodelsur.mlkit.feature.posedetection.presentation

import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.component.CameraPermissionRequester
import com.codigodelsur.mlkit.core.presentation.component.MlkCameraPreview
import com.codigodelsur.mlkit.core.presentation.component.MlkTopAppBar
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme
import com.codigodelsur.mlkit.feature.posedetection.presentation.analyzer.PoseDetectionAnalyzer
import com.codigodelsur.mlkit.feature.posedetection.presentation.classification.PoseClassificationResult
import com.codigodelsur.mlkit.feature.posedetection.presentation.component.PoseLegend
import com.codigodelsur.mlkit.feature.posedetection.presentation.component.PoseOverlay
import com.codigodelsur.mlkit.feature.posedetection.presentation.model.PPose

@Composable
fun PoseDetectionRoute(
    modifier: Modifier = Modifier,
    viewModel: PoseDetectionViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    PoseDetectionScreen(
        modifier = modifier,
        detectedPose = state.detectedPose,
        inputImageWidth = state.inputImageWidth,
        inputImageHeight = state.inputImageHeight,
        reps = state.reps,
        className = state.className,
        onPoseDetected = { pose, classification, inputImageWidth, inputImageHeight ->
            viewModel.updateDetectedPose(
                pose,
                classification,
                inputImageWidth,
                inputImageHeight
            )
        },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PoseDetectionScreen(
    modifier: Modifier,
    detectedPose: PPose?,
    inputImageWidth: Int,
    inputImageHeight: Int,
    reps: Int,
    className: String?,
    onPoseDetected: (PPose?, PoseClassificationResult?, Int, Int) -> Unit,
    onBackClick: () -> Unit
) {
    val currentOnPoseDetected by rememberUpdatedState(onPoseDetected)
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    Column(modifier = modifier.fillMaxSize()) {
        MlkTopAppBar(
            titleRes = R.string.feature_pose_detection_title,
            onNavigationClick = onBackClick
        )
        CameraPermissionRequester(
            modifier = Modifier.weight(1.0f)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                MlkCameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    scaleType = PreviewView.ScaleType.FIT_CENTER, // Needed to position the overlay correctly
                    cameraSelector = cameraSelector,
                    onSwitchCamera = { cameraSelector = it },
                    setUpDetector = { cameraController, context ->
                        cameraController.setImageAnalysisAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            PoseDetectionAnalyzer(
                                context = context,
                                onPoseDetected = currentOnPoseDetected
                            )
                        )
                    }
                ) {
                    if (detectedPose != null) {
                        PoseOverlay(
                            modifier = Modifier.fillMaxSize(),
                            pose = detectedPose,
                            inputImageWidth = inputImageWidth,
                            inputImageHeight = inputImageHeight,
                            mirror = cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA
                        )
                    }
                }

                PoseLegend(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 16.dp),
                    reps = reps,
                    className = className
                )
            }
        }
    }
}

@Preview
@Composable
private fun PoseDetectionScreenPreview() {
    MlkTheme {
        PoseDetectionScreen(
            modifier = Modifier.fillMaxSize(),
            detectedPose = null,
            inputImageWidth = 1,
            inputImageHeight = 1,
            reps = 0,
            className = null,
            onPoseDetected = { _, _, _, _ -> },
            onBackClick = {}
        )
    }
}