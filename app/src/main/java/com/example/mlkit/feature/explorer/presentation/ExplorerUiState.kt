package com.example.mlkit.feature.explorer.presentation

import com.example.mlkit.core.model.PMLKitFeature

data class ExplorerUiState(
    val features: List<PMLKitFeature> = listOf(PMLKitFeature.DocumentScanner())
)