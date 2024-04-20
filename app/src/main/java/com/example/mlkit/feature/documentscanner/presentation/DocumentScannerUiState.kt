package com.example.mlkit.feature.documentscanner.presentation

import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult

data class DocumentScannerUiState(
    val scanningResult: GmsDocumentScanningResult? = null,
    val scannerError: String? = null,
    val saveSuccess: Boolean = false
)