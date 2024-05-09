package com.codigodelsur.mlkit.feature.objectdetection.presentation

import com.google.mlkit.vision.objects.DetectedObject

data class ObjectDetectionUiState(
    val detectedObjects: List<DetectedObject> = emptyList(),
    val hideUnlabeled: Boolean = false,
)