package com.codigodelsur.mlkit.feature.posedetection.presentation

import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.component.CameraPermissionRequester
import com.codigodelsur.mlkit.core.presentation.component.MlkCameraPreview
import com.codigodelsur.mlkit.core.presentation.component.MlkTopAppBar
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme
import com.codigodelsur.mlkit.feature.posedetection.presentation.analyzer.PoseDetectionAnalyzer
import com.codigodelsur.mlkit.feature.posedetection.presentation.classification.PoseClassificationResult
import com.codigodelsur.mlkit.feature.posedetection.presentation.model.PPose
import com.google.mlkit.vision.common.PointF3D

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

@Composable
private fun PoseOverlay(
    modifier: Modifier,
    pose: PPose,
    inputImageWidth: Int,
    inputImageHeight: Int,
    mirror: Boolean
) {
    Canvas(modifier = modifier.clipToBounds()) {
        withTransform(
            {
                scale(scaleX = if (mirror) -1.0f else 1.0f, scaleY = 1.0f)
            }
        ) {
            val scaleX = size.width / inputImageHeight
            val scaleY = size.height / inputImageWidth
            with(pose) {
                // Face
                drawLandmarksLine(leftMouth, rightMouth, scaleX, scaleY)
                drawLandmarksLine(leftEyeOuter, leftEye, scaleX, scaleY)
                drawLandmarksLine(leftEye, leftEyeInner, scaleX, scaleY)
                drawLandmarksLine(rightEyeOuter, rightEye, scaleX, scaleY)
                drawLandmarksLine(rightEye, rightEyeInner, scaleX, scaleY)
                drawLandmarksLine(rightEyeOuter, rightEar, scaleX, scaleY)
                drawLandmarksLine(leftEyeOuter, leftEar, scaleX, scaleY)

                // Body
                drawLandmarksLine(leftShoulder, rightShoulder, scaleX, scaleY)
                drawLandmarksLine(rightShoulder, rightHip, scaleX, scaleY)
                drawLandmarksLine(rightHip, leftHip, scaleX, scaleY)
                drawLandmarksLine(leftHip, leftShoulder, scaleX, scaleY)

                // Left Arm
                drawLandmarksLine(leftShoulder, leftElbow, scaleX, scaleY)
                drawLandmarksLine(leftElbow, leftWrist, scaleX, scaleY)
                drawLandmarksLine(leftWrist, leftThumb, scaleX, scaleY)
                drawLandmarksLine(leftWrist, leftIndex, scaleX, scaleY)
                drawLandmarksLine(leftWrist, leftPinky, scaleX, scaleY)
                drawLandmarksLine(leftIndex, leftPinky, scaleX, scaleY)

                // Right Arm
                drawLandmarksLine(rightShoulder, rightElbow, scaleX, scaleY)
                drawLandmarksLine(rightElbow, rightWrist, scaleX, scaleY)
                drawLandmarksLine(rightWrist, rightThumb, scaleX, scaleY)
                drawLandmarksLine(rightWrist, rightIndex, scaleX, scaleY)
                drawLandmarksLine(rightWrist, rightPinky, scaleX, scaleY)
                drawLandmarksLine(rightIndex, rightPinky, scaleX, scaleY)

                // Left Leg
                drawLandmarksLine(leftHip, leftKnee, scaleX, scaleY)
                drawLandmarksLine(leftKnee, leftAnkle, scaleX, scaleY)
                drawLandmarksLine(leftAnkle, leftHeel, scaleX, scaleY)
                drawLandmarksLine(leftHeel, leftFootIndex, scaleX, scaleY)
                drawLandmarksLine(leftFootIndex, leftAnkle, scaleX, scaleY)

                // Right Leg
                drawLandmarksLine(rightHip, rightKnee, scaleX, scaleY)
                drawLandmarksLine(rightKnee, rightAnkle, scaleX, scaleY)
                drawLandmarksLine(rightAnkle, rightHeel, scaleX, scaleY)
                drawLandmarksLine(rightHeel, rightFootIndex, scaleX, scaleY)
                drawLandmarksLine(rightFootIndex, rightAnkle, scaleX, scaleY)

                allLandmarks.forEach { landmark ->
                    drawLandmark(landmark, scaleX, scaleY)
                }
            }
        }
    }
}

private fun DrawScope.drawLandmark(position: PointF3D, scaleX: Float, scaleY: Float) {
    drawCircle(
        color = Color.Red,
        radius = 10f,
        center = Offset(position.x * scaleX, position.y * scaleY)
    )
}

private fun DrawScope.drawLandmarksLine(
    start: PointF3D,
    end: PointF3D,
    scaleX: Float,
    scaleY: Float
) {

    drawLine(
        color = Color.White,
        start = Offset(start.x * scaleX, start.y * scaleY),
        end = Offset(end.x * scaleX, end.y * scaleY),
        strokeWidth = 2.0f
    )
}

@Composable
private fun PoseLegend(
    modifier: Modifier,
    reps: Int,
    className: String?
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.inverseSurface)
            .padding(8.dp)
            .width(200.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(id = R.string.pose_detection_reps_counter, reps),
            color = MaterialTheme.colorScheme.inverseOnSurface
        )
        Text(
            text = stringResource(
                id = R.string.pose_detection_classification, className ?: stringResource(
                    id = R.string.pose_detection_classification_none
                )
            ),
            color = MaterialTheme.colorScheme.inverseOnSurface
        )
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