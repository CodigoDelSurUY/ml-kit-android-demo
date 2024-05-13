package com.codigodelsur.mlkit.feature.posedetection.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import com.codigodelsur.mlkit.feature.posedetection.presentation.model.PPose
import com.google.mlkit.vision.common.PointF3D

@Composable
fun PoseOverlay(
    modifier: Modifier = Modifier,
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
            // Scale the pose from the input image coordinates to the canvas coordinates
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