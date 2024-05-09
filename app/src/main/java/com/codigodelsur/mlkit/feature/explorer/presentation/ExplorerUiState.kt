package com.codigodelsur.mlkit.feature.explorer.presentation

import com.codigodelsur.mlkit.core.presentation.model.PMLKitFeature

data class ExplorerUiState(
    val features: List<PMLKitFeature> = PMLKitFeature.all
)