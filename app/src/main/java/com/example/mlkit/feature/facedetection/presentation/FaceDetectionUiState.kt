package com.example.mlkit.feature.facedetection.presentation

import com.google.mlkit.vision.face.Face

data class FaceDetectionUiState(
    val detectedFaces: List<Face> = emptyList()
)