package com.codigodelsur.mlkit.feature.facedetection.presentation.component

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.codigodelsur.mlkit.R
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos

@Composable
fun AccessoriesOverlay(
    modifier: Modifier,
    mirror: Boolean,
    faces: List<Face>
) {
    val sunglassesBitmap = ImageBitmap.imageResource(id = R.drawable.sunglasses)
    val clownNoseBitmap = ImageBitmap.imageResource(id = R.drawable.clown_nose)
    Canvas(modifier = modifier.clipToBounds()) {
        for (face in faces) {
            drawSunglasses(sunglassesBitmap, face, mirror)
            drawClownNose(clownNoseBitmap, face)
        }
    }
}

private fun DrawScope.drawSunglasses(sunglassesBitmap: ImageBitmap, face: Face, mirror: Boolean) {
    val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)?.position ?: return
    val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)?.position ?: return
    val faceBox = face.boundingBox.toComposeRect()
    val midpoint = calculateMidPoint(leftEye, rightEye)
    val scale = faceBox.width / sunglassesBitmap.width
    val rotationAngle = if (mirror) {
        calculateRotationAngle(rightEye, leftEye)
    } else {
        calculateRotationAngle(leftEye, rightEye)
    }
    val newWidth = sunglassesBitmap.width * scale * cos(rotationAngle * (PI / 180)).toFloat()


    withTransform({
        rotate(degrees = rotationAngle, pivot = Offset(midpoint.x, midpoint.y))

        // Translate to the midpoint, adjusting for the rotation
        translate(
            left = midpoint.x - newWidth / 2,
            // Just a hardcoded adjustment based on the glasses image
            top = midpoint.y - ((sunglassesBitmap.height * scale / 2) * 0.9f)
        )
        scale(scale, scale, Offset.Zero)
    }) {
        drawImage(sunglassesBitmap)
    }
}

private fun DrawScope.drawClownNose(clownNoseBitmap: ImageBitmap, face: Face) {
    val nose = face.getLandmark(FaceLandmark.NOSE_BASE)?.position ?: return
    val faceBox = face.boundingBox.toComposeRect()
    val noseWidth = clownNoseBitmap.width
    val noseHeight = clownNoseBitmap.height
    val faceWidth = faceBox.width
    // Make it so that the nose is 1/4 of the width of a face
    val scale = (faceWidth / noseWidth) / 4
    val scaledWidth = (noseWidth * scale).toInt()
    val scaledHeight = (noseHeight * scale).toInt()
    val noseX = (nose.x - scaledWidth / 2).toInt()
    val noseY = (nose.y - scaledHeight / 2).toInt()
    drawImage(
        image = clownNoseBitmap,
        srcOffset = IntOffset.Zero,
        srcSize = IntSize(noseWidth, noseHeight),
        dstOffset = IntOffset(noseX, noseY),
        dstSize = IntSize(scaledWidth, scaledHeight),
        filterQuality = FilterQuality.High
    )
}

private fun calculateRotationAngle(startPoint: PointF, endPoint: PointF): Float {
    val deltaX = endPoint.x - startPoint.x
    val deltaY = endPoint.y - startPoint.y
    val radians = atan2(deltaY, deltaX)
    return radians * (180 / PI).toFloat()
}

private fun calculateMidPoint(point1: PointF, point2: PointF): Offset {
    return Offset(
        x = (point2.x + point1.x) / 2,
        y = (point2.y + point1.y) / 2
    )
}
