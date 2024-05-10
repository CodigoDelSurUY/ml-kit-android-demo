package com.codigodelsur.mlkit.feature.documentscanner.presentation

import androidx.lifecycle.ViewModel
import com.codigodelsur.mlkit.feature.documentscanner.presentation.model.PScannedDocument
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DocumentScannerViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(DocumentScannerUiState())
    val state: StateFlow<DocumentScannerUiState> = _state.asStateFlow()

    fun updateDocumentScan(result: GmsDocumentScanningResult?) {
        val pdf = result?.pdf?.uri
        val pages = result?.pages?.map { page -> page.imageUri }
        if (pdf != null && pages != null) {
            _state.update {
                it.copy(
                    scannedDocument = PScannedDocument(pdf = pdf, pages = pages)
                )
            }
        }
    }

    fun showScannerError(error: String) {
        _state.update { it.copy(scannerError = error) }
    }

    fun hideScannerError() {
        _state.update { it.copy(scannerError = null) }
    }

    fun showSaveSuccess() {
        _state.update {
            it.copy(
                scannedDocument = null,
                saveSuccess = true
            )
        }

    }

    fun hideSaveSuccess() {
        _state.update { it.copy(saveSuccess = false) }
    }
}