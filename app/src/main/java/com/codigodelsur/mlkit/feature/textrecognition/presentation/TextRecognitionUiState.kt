package com.codigodelsur.mlkit.feature.textrecognition.presentation

import com.google.mlkit.vision.text.Text

data class TextRecognitionUiState(
    val recognizedText: Text? = null
)