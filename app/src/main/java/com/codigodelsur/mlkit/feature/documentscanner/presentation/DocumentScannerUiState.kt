package com.codigodelsur.mlkit.feature.documentscanner.presentation


import com.codigodelsur.mlkit.feature.documentscanner.presentation.model.PScannedDocument

data class DocumentScannerUiState(
    val scannedDocument: PScannedDocument? = null,
    val scannerError: String? = null,
    val saveSuccess: Boolean = false
)