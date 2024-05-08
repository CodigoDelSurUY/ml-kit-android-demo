package com.example.mlkit.feature.posedetection.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mlkit.R
import com.example.mlkit.app.ui.theme.MlkTheme
import com.example.mlkit.core.presentation.component.CameraPermissionRequester
import com.example.mlkit.core.presentation.component.MlkCameraPreview
import com.example.mlkit.core.presentation.component.MlkTopAppBar
import com.example.mlkit.feature.posedetection.presentation.classification.PoseClassificationResult
import com.example.mlkit.feature.posedetection.presentation.classification.PoseClassifierProcessor
import com.example.mlkit.feature.posedetection.presentation.model.PPose
import com.example.mlkit.feature.posedetection.presentation.model.toPresentation
import com.google.mlkit.vision.common.PointF3D
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions

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
        reps = state.reps,
        className = state.className,
        onPoseDetected = { pose, classification ->
            viewModel.updateDetectedPose(
                pose,
                classification
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
    reps: Int,
    className: String?,
    onPoseDetected: (PPose?, PoseClassificationResult?) -> Unit,
    onBackClick: () -> Unit
) {
    val currentOnPoseDetected by rememberUpdatedState(onPoseDetected)
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
                    setUpDetector = { cameraController, context ->
                        val options = AccuratePoseDetectorOptions.Builder()
                            .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
                            .build()
                        val poseDetector = PoseDetection.getClient(options)
                        val poseClassifier = PoseClassifierProcessor(context, true)
                        val executor = ContextCompat.getMainExecutor(context)
                        cameraController.setImageAnalysisAnalyzer(
                            executor,
                            MlKitAnalyzer(
                                listOf(poseDetector),
                                // This coordinate system does not seem to work for Pose Detection
                                // Similar error to https://issuetracker.google.com/issues/292404395
                                ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
                                executor
                            ) { result: MlKitAnalyzer.Result? ->
                                val pose = result?.getValue(poseDetector)
                                val classification = if (pose == null) {
                                    null
                                } else {
                                    poseClassifier.getPoseResult(pose)
                                }
                                currentOnPoseDetected(pose?.toPresentation(), classification)
                            }
                        )
                    }
                ) {
                    if (detectedPose != null) {
                        PoseOverlay(
                            modifier = Modifier.fillMaxSize(),
                            pose = detectedPose,
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
    pose: PPose
) {
    Canvas(modifier = modifier.clipToBounds()) {
        with(pose) {
            // Face
            drawLandmarksLine(leftMouth, rightMouth)
            drawLandmarksLine(leftEyeOuter, leftEye)
            drawLandmarksLine(leftEye, leftEyeInner)
            drawLandmarksLine(rightEyeOuter, rightEye)
            drawLandmarksLine(rightEye, rightEyeInner)
            drawLandmarksLine(rightEyeOuter, rightEar)
            drawLandmarksLine(leftEyeOuter, leftEar)

            // Body
            drawLandmarksLine(leftShoulder, rightShoulder)
            drawLandmarksLine(rightShoulder, rightHip)
            drawLandmarksLine(rightHip, leftHip)
            drawLandmarksLine(leftHip, leftShoulder)

            // Left Arm
            drawLandmarksLine(leftShoulder, leftElbow)
            drawLandmarksLine(leftElbow, leftWrist)
            drawLandmarksLine(leftWrist, leftThumb)
            drawLandmarksLine(leftWrist, leftIndex)
            drawLandmarksLine(leftWrist, leftPinky)
            drawLandmarksLine(leftIndex, leftPinky)

            // Right Arm
            drawLandmarksLine(rightShoulder, rightElbow)
            drawLandmarksLine(rightElbow, rightWrist)
            drawLandmarksLine(rightWrist, rightThumb)
            drawLandmarksLine(rightWrist, rightIndex)
            drawLandmarksLine(rightWrist, rightPinky)
            drawLandmarksLine(rightIndex, rightPinky)

            // Left Leg
            drawLandmarksLine(leftHip, leftKnee)
            drawLandmarksLine(leftKnee, leftAnkle)
            drawLandmarksLine(leftAnkle, leftHeel)
            drawLandmarksLine(leftHeel, leftFootIndex)
            drawLandmarksLine(leftFootIndex, leftAnkle)

            // Right Leg
            drawLandmarksLine(rightHip, rightKnee)
            drawLandmarksLine(rightKnee, rightAnkle)
            drawLandmarksLine(rightAnkle, rightHeel)
            drawLandmarksLine(rightHeel, rightFootIndex)
            drawLandmarksLine(rightFootIndex, rightAnkle)

            allLandmarks.forEach { landmark ->
                drawLandmark(landmark)
            }
        }
    }
}

private fun DrawScope.drawLandmark(position: PointF3D) {
    drawCircle(color = Color.Red, radius = 10f, center = Offset(position.x, position.y))
}

private fun DrawScope.drawLandmarksLine(start: PointF3D, end: PointF3D) {
    drawLine(
        color = Color.White,
        start = Offset(start.x, start.y),
        end = Offset(end.x, end.y),
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
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .width(200.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = stringResource(id = R.string.pose_detection_reps_counter, reps))
        Text(
            text = stringResource(
                id = R.string.pose_detection_classification, className ?: stringResource(
                    id = R.string.pose_detection_classification_none
                )
            )
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
            reps = 0,
            className = null,
            onPoseDetected = { _, _ -> },
            onBackClick = {}
        )
    }
}