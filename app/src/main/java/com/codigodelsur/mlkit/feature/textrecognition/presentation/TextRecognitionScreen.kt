package com.codigodelsur.mlkit.feature.textrecognition.presentation

import androidx.compose.foundation.background
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme
import com.codigodelsur.mlkit.core.presentation.component.CameraPermissionRequester
import com.codigodelsur.mlkit.core.presentation.component.MlkCameraPreview
import com.codigodelsur.mlkit.core.presentation.component.MlkTopAppBar
import com.codigodelsur.mlkit.feature.textrecognition.presentation.analyzer.TextRecognitionAnalyzer

@Composable
fun TextRecognitionRoute(
    modifier: Modifier = Modifier,
    viewModel: TextRecognitionViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TextRecognitionScreen(
        modifier = modifier,
        recognizedText = state.recognizedText,
        onTextRecognized = { viewModel.updateRecognizedText(text = it) },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextRecognitionScreen(
    modifier: Modifier = Modifier,
    recognizedText: String?,
    onTextRecognized: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val currentOnTextRecognized by rememberUpdatedState(onTextRecognized)
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
                MlkCameraPreview(
                    modifier = Modifier.weight(3.0f),
                    setUpDetector = { cameraController, context ->
                        cameraController.setImageAnalysisAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            TextRecognitionAnalyzer(onTextRecognized = currentOnTextRecognized)
                        )
                    }
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
                        text = recognizedText ?: "",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TextRecognitionScreenPreview() {
    MlkTheme {
        TextRecognitionScreen(
            recognizedText = "Test Text",
            onTextRecognized = {},
            onBackClick = {})
    }
}