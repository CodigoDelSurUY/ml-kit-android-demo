package com.codigodelsur.mlkit.feature.facedetection.presentation

import com.google.mlkit.vision.face.Face

data class FaceDetectionUiState(
    val detectedFaces: List<Face> = emptyList(),
    val isSmiling: Boolean = false,
    val showBoundingBoxes: Boolean = true,
    val showHat: Boolean = false
)