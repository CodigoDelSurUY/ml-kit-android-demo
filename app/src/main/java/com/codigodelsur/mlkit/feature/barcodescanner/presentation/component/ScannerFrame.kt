package com.codigodelsur.mlkit.feature.barcodescanner.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme

@Composable
fun ScannerFrame(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val strokeWidth = 6.dp.toPx()
        val cornerLength = 50.dp.toPx() // Length of the lines in each corner
        val cornerRadius = 20.dp.toPx() // Radius of the rounded corners
        val frameBrush = SolidColor(Color.White)

        // Calculate frame dimensions
        val frameWidth = size.width * 0.75f
        val frameHeight = size.height * 0.5f
        val leftX = (size.width - frameWidth) / 2
        val topY = (size.height - frameHeight) / 2
        val rightX = leftX + frameWidth
        val bottomY = topY + frameHeight

        // Draw rounded corners using arcs and lines
        // Top-left corner
        drawArc(
            brush = frameBrush,
            startAngle = 180f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(leftX, topY),
            size = Size(cornerRadius * 2, cornerRadius * 2),
            style = Stroke(width = strokeWidth)
        )
        drawLine(
            brush = frameBrush,
            start = Offset(leftX + cornerRadius, topY),
            end = Offset(leftX + cornerRadius + cornerLength, topY),
            strokeWidth = strokeWidth
        )
        drawLine(
            brush = frameBrush,
            start = Offset(leftX, topY + cornerRadius),
            end = Offset(leftX, topY + cornerRadius + cornerLength),
            strokeWidth = strokeWidth
        )

        // Top-right corner
        drawArc(
            brush = frameBrush,
            startAngle = 270f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(rightX - 2 * cornerRadius, topY),
            size = Size(cornerRadius * 2, cornerRadius * 2),
            style = Stroke(width = strokeWidth)
        )
        drawLine(
            brush = frameBrush,
            start = Offset(rightX - cornerRadius - cornerLength, topY),
            end = Offset(rightX - cornerRadius, topY),
            strokeWidth = strokeWidth
        )
        drawLine(
            brush = frameBrush,
            start = Offset(rightX, topY + cornerRadius),
            end = Offset(rightX, topY + cornerRadius + cornerLength),
            strokeWidth = strokeWidth
        )

        // Bottom-left corner
        drawArc(
            brush = frameBrush,
            startAngle = 90f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(leftX, bottomY - 2 * cornerRadius),
            size = Size(cornerRadius * 2, cornerRadius * 2),
            style = Stroke(width = strokeWidth)
        )
        drawLine(
            brush = frameBrush,
            start = Offset(leftX, bottomY - cornerRadius - cornerLength),
            end = Offset(leftX, bottomY - cornerRadius),
            strokeWidth = strokeWidth
        )
        drawLine(
            brush = frameBrush,
            start = Offset(leftX + cornerRadius, bottomY),
            end = Offset(leftX + cornerRadius + cornerLength, bottomY),
            strokeWidth = strokeWidth
        )

        // Bottom-right corner
        drawArc(
            brush = frameBrush,
            startAngle = 0f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(rightX - 2 * cornerRadius, bottomY - 2 * cornerRadius),
            size = Size(cornerRadius * 2, cornerRadius * 2),
            style = Stroke(width = strokeWidth)
        )
        drawLine(
            brush = frameBrush,
            start = Offset(rightX - cornerRadius - cornerLength, bottomY),
            end = Offset(rightX - cornerRadius, bottomY),
            strokeWidth = strokeWidth
        )
        drawLine(
            brush = frameBrush,
            start = Offset(rightX, bottomY - cornerRadius - cornerLength),
            end = Offset(rightX, bottomY - cornerRadius),
            strokeWidth = strokeWidth
        )
    }
}

@Preview
@Composable
private fun ScannerFramePreview() {
    MlkTheme {
        ScannerFrame()
    }
}