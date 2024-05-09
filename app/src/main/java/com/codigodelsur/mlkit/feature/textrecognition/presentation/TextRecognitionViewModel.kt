package com.codigodelsur.mlkit.feature.textrecognition.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TextRecognitionViewModel  @Inject constructor(): ViewModel()  {
    private val _state = MutableStateFlow(TextRecognitionUiState())
    val state: StateFlow<TextRecognitionUiState> = _state.asStateFlow()

    fun updateRecognizedText(text: String) {
        _state.update { it.copy(recognizedText = text) }
    }
}