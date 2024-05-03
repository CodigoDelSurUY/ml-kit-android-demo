package com.example.mlkit.feature.barcodescanner.presentation

data class BarcodeScannerUiState(
    val isLoading: Boolean = false,
    val isEnabled: Boolean = false
)

sealed class BarcodeScannerEffect {
    data class OpenWebsite(val url: String) : BarcodeScannerEffect()
}