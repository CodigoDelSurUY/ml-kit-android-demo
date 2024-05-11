package com.codigodelsur.mlkit.feature.posedetection.presentation

import com.codigodelsur.mlkit.feature.posedetection.presentation.model.PPose

data class PoseDetectionUiState(
    val detectedPose: PPose? = null,
    val reps: Int = 0,
    val className: String? = null,
    val inputImageWidth: Int = 1,
    val inputImageHeight: Int = 1
)