package com.codigodelsur.mlkit.feature.barcodescanner.presentation

import android.content.Intent
import android.net.Uri
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
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.component.CameraPermissionRequester
import com.codigodelsur.mlkit.core.presentation.component.MlkCameraPreview
import com.codigodelsur.mlkit.core.presentation.component.MlkTopAppBar
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme
import com.codigodelsur.mlkit.feature.barcodescanner.presentation.analyzer.BarcodeScannerAnalyzer
import com.codigodelsur.mlkit.feature.barcodescanner.presentation.component.ScannerFrame
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
        viewModel.effects.collect {
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
                MlkCameraPreview(
                    modifier = Modifier.fillMaxSize(),
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

@Preview
@Composable
private fun BarcodeScannerScreenPreview() {
    MlkTheme {
        BarcodeScannerScreen(isLoading = true, onBarcodesDetected = {}, onBackClick = {})
    }
}