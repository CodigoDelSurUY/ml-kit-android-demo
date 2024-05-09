package com.example.mlkit.feature.barcodescanner.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mlkit.R
import com.example.mlkit.core.presentation.theme.MlkTheme
import com.example.mlkit.core.presentation.component.CameraPermissionRequester
import com.example.mlkit.core.presentation.component.MlkCameraPreview
import com.example.mlkit.core.presentation.component.MlkTopAppBar
import com.example.mlkit.feature.barcodescanner.presentation.analyzer.BarcodeScannerAnalyzer
import com.google.mlkit.vision.barcode.common.Barcode

@Composable
fun BarcodeScannerRoute(
    modifier: Modifier = Modifier,
    viewModel: BarcodeScannerViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.effects.collect{
            it.let {
                when (it) {
                    is BarcodeScannerEffect.OpenWebsite -> {
                        val openURL = Intent(Intent.ACTION_VIEW)
                        openURL.data = Uri.parse(it.url)
                        startActivity(context, openURL, null)
                    }
                }
            }
        }
    }

    // This is just a workaround to avoid multiple detections of the same QR code
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.enableScanner()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    BarcodeScannerScreen(
        modifier = modifier,
        isLoading = state.isLoading,
        onBarcodesDetected = { viewModel.processBarcodes(barcodes = it) },
        onBackClick = onBackClick
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BarcodeScannerScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onBarcodesDetected: (List<Barcode>) -> Unit,
    onBackClick: () -> Unit
) {
    val currentOnBarcodeDetected by rememberUpdatedState(onBarcodesDetected)
    Column(modifier = modifier.fillMaxSize()) {
        MlkTopAppBar(
            titleRes = R.string.feature_barcode_scanner_title,
            onNavigationClick = onBackClick
        )
        CameraPermissionRequester(
            modifier = Modifier.weight(1.0f)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                MlkCameraPreview(modifier = Modifier.fillMaxSize(),
                    setUpDetector = { cameraController, context ->
                        cameraController.setImageAnalysisAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            BarcodeScannerAnalyzer(onBarcodesDetected = currentOnBarcodeDetected)
                        )
                    })

                ScannerFrame(modifier = Modifier.fillMaxSize())

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }
        }
    }
}

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
private fun BarcodeScannerScreenPreview() {
    MlkTheme {
        BarcodeScannerScreen(isLoading = true, onBarcodesDetected = {}, onBackClick = {})
    }
}