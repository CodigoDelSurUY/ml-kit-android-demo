package com.codigodelsur.mlkit.feature.objectdetection.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.codigodelsur.mlkit.core.presentation.theme.Typography
import com.codigodelsur.mlkit.core.presentation.util.getForId
import com.google.mlkit.vision.objects.DetectedObject

@Composable
fun ObjectBoundingBoxesOverlay(
    modifier: Modifier,
    hideUnlabeled: Boolean,
    objects: List<DetectedObject>
) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier = modifier.clipToBounds()) {
        for (detectedObject in objects) {
            val label = detectedObject.labels.maxByOrNull { it.confidence }

            val color = Color.getForId(detectedObject.trackingId ?: 1)
            // Draw the rectangle
            val boxRect = detectedObject.boundingBox.toComposeRect()
            if (label != null || !hideUnlabeled) {
                drawRect(
                    color = color,
                    topLeft = boxRect.topLeft,
                    size = boxRect.size,
                    style = Stroke(width = 3.dp.toPx())
                )
            }
            if (label != null) {
                val legend = "${label.text} - ${"%.2f".format(label.confidence)}"
                val legendStyle = Typography.labelMedium.copy(color = color)

                val measure = textMeasurer.measure(
                    text = legend,
                    style = legendStyle
                )
                drawText(
                    textMeasurer = textMeasurer,
                    text = legend,
                    style = legendStyle,
                    topLeft = Offset(
                        x = boxRect.left,
                        y = boxRect.top - measure.getBoundingBox(0).height
                    )
                )
            }
        }
    }
}