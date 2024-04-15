package com.example.mlkit.feature.textrecognition.presentation

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.AspectRatio
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.mlkit.R
import com.example.mlkit.app.ui.theme.MlkTheme
import com.example.mlkit.core.presentation.component.CameraPermissionRequester
import com.example.mlkit.core.presentation.component.MlkTopAppBar
import com.example.mlkit.feature.textrecognition.presentation.util.TextRecognitionAnalyzer

@Composable
fun TextRecognitionRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    var detectedText: String? by remember { mutableStateOf(null) }
    TextRecognitionScreen(
        modifier = modifier,
        detectedText = detectedText,
        onDetectedTextUpdated = { detectedText = it },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextRecognitionScreen(
    modifier: Modifier = Modifier,
    detectedText: String?,
    onDetectedTextUpdated: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        MlkTopAppBar(
            titleRes = R.string.feature_text_recognition_title,
            onNavigationClick = onBackClick
        )
        CameraPermissionRequester(
            modifier = modifier.weight(1.0f)
        ) {
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                CameraPreview(
                    modifier = Modifier.weight(3.0f),
                    onDetectedTextUpdated = onDetectedTextUpdated
                )
                Column(
                    modifier = Modifier
                        .weight(1.0f)
                        .verticalScroll(
                            rememberScrollState()
                        )
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = detectedText ?: "",
                    )
                }
            }
        }
    }
}

@Composable
private fun CameraPreview(
    modifier: Modifier,
    onDetectedTextUpdated: (String) -> Unit
) {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController =
        remember { LifecycleCameraController(context) }
    AndroidView(
        modifier = modifier,
        factory = {
            PreviewView(it).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(Color.BLACK)
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_START
            }.also { previewView ->
                cameraController.imageAnalysisTargetSize = CameraController.OutputSize(AspectRatio.RATIO_16_9)
                cameraController.setImageAnalysisAnalyzer(
                    ContextCompat.getMainExecutor(it),
                    TextRecognitionAnalyzer(onDetectedTextUpdated = onDetectedTextUpdated)
                )

                cameraController.bindToLifecycle(lifecycleOwner)
                previewView.controller = cameraController
            }
        }
    )
}

@Preview
@Composable
private fun TextRecognitionScreenPreview() {
    MlkTheme {
        TextRecognitionScreen(
            detectedText = "Test Text",
            onDetectedTextUpdated = {},
            onBackClick = {})
    }
}