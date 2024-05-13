package com.codigodelsur.mlkit.feature.selfiesegmentation.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.google.mlkit.vision.segmentation.SegmentationMask
import java.nio.ByteBuffer

@Composable
fun SelfieMaskOverlay(
    modifier: Modifier,
    selfieMask: SegmentationMask,
    foregroundThreshold: Float
) {
    Canvas(
        modifier = modifier
            .clipToBounds()
    ) {
        drawSegmentationMask(
            buffer = selfieMask.buffer,
            width = selfieMask.width,
            height = selfieMask.height,
            foregroundThreshold = foregroundThreshold
        )
    }
}

private fun DrawScope.drawSegmentationMask(
    buffer: ByteBuffer,
    width: Int,
    height: Int,
    foregroundThreshold: Float
) {
    val scaleX = size.width / width
    val scaleY = size.height / height
    for (y in 0 until height) {
        for (x in 0 until width) {
            val color =
                if (buffer.getFloat() > foregroundThreshold) Color.Red.copy(alpha = 0.8f) else Color.Transparent
            val positionX = size.width - scaleX * x // Mirrored due to front camera
            val positionY = y * scaleY
            drawRect(
                color = color,
                topLeft = Offset(positionX, positionY),
                size = Size(scaleX, scaleY)
            )
        }
    }
}