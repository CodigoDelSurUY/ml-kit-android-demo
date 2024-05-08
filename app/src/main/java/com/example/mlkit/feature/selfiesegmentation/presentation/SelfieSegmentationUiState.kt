package com.example.mlkit.feature.selfiesegmentation.presentation

import com.google.mlkit.vision.segmentation.SegmentationMask

data class SelfieSegmentationUiState(
    val selfieMask: SegmentationMask? =  null,
    val foregroundThreshold: Float = 0.2f
)