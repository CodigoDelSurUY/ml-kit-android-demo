package com.example.mlkit.feature.explorer.presentation

import com.example.mlkit.core.presentation.model.PMLKitFeature

data class ExplorerUiState(
    val features: List<PMLKitFeature> = PMLKitFeature.all
)