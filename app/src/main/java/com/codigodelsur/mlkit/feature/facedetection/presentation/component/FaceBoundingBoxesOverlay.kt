package com.codigodelsur.mlkit.feature.facedetection.presentation.component

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark

@Composable
fun FaceBoundingBoxesOverlay(
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
            face.getLandmark(FaceLandmark.LEFT_EYE)?.position?.let { drawLandmark(it) }
            face.getLandmark(FaceLandmark.RIGHT_EYE)?.position?.let { drawLandmark(it) }
            face.getLandmark(FaceLandmark.NOSE_BASE)?.position?.let { drawLandmark(it) }
            val mouthLeft = face.getLandmark(FaceLandmark.MOUTH_LEFT)?.position
            val mouthRight = face.getLandmark(FaceLandmark.MOUTH_RIGHT)?.position
            val mouthBottom = face.getLandmark(FaceLandmark.MOUTH_BOTTOM)?.position
            mouthLeft?.let { drawLandmark(it) }
            mouthRight?.let { drawLandmark(it) }
            mouthBottom?.let { drawLandmark(it) }
            if (mouthBottom != null) {
                if (mouthLeft != null) {
                    drawLandmarksLine(mouthLeft, mouthBottom)
                }
                if (mouthRight != null) {
                    drawLandmarksLine(mouthRight, mouthBottom)
                }
            }
        }
    }
}

private fun DrawScope.drawLandmark(position: PointF) {
    drawCircle(color = Color.Red, radius = 10f, center = Offset(position.x, position.y))
}

private fun DrawScope.drawLandmarksLine(start: PointF, end: PointF) {
    drawLine(
        color = Color.White,
        start = Offset(start.x, start.y),
        end = Offset(end.x, end.y),
        strokeWidth = 2.0f
    )
}