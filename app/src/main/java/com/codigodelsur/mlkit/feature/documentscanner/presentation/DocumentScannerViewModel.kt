package com.codigodelsur.mlkit.feature.documentscanner.presentation

import androidx.lifecycle.ViewModel
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
        _state.update { it.copy(scanningResult = result) }
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
                scanningResult = null,
                saveSuccess = true
            )
        }

    }

    fun hideSaveSuccess() {
        _state.update { it.copy(saveSuccess = false) }
    }
}