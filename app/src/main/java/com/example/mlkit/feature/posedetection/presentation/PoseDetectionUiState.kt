package com.example.mlkit.feature.posedetection.presentation

import com.example.mlkit.feature.posedetection.presentation.model.PPose

data class PoseDetectionUiState(
    val detectedPose: PPose? = null,
    val reps: Int = 0,
    val className: String? = null
)