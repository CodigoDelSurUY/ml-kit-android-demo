package com.codigodelsur.mlkit.feature.textrecognition.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.text.Text

@Composable
fun TextBoundingBoxesOverlay(
    modifier: Modifier,
    text: Text
) {
    Canvas(modifier = modifier.clipToBounds()) {
        for (block in text.textBlocks) {
            block.boundingBox?.toComposeRect()?.let { boxRect ->
                drawRect(
                    color = Color.Blue,
                    topLeft = boxRect.topLeft,
                    size = boxRect.size,
                    style = Stroke(width = 3.dp.toPx())
                )
            }
        }
    }
}