package com.codigodelsur.mlkit.feature.barcodescanning.presentation

data class BarcodeScanningUiState(
    val isLoading: Boolean = false,
    val isEnabled: Boolean = false
)

sealed class BarcodeScanningEffect {
    data class OpenWebsite(val url: String) : BarcodeScanningEffect()
}