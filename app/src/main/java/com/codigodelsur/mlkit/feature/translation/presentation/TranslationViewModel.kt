package com.codigodelsur.mlkit.feature.translation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codigodelsur.mlkit.feature.translation.domain.usecase.TranslateToEnglishUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslationViewModel @Inject constructor(
    private val translateToEnglishUseCase: TranslateToEnglishUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(TranslationUIState())
    val state: StateFlow<TranslationUIState> = _state.asStateFlow()


    fun updateInputText(text: String) {
        _state.update {
            it.copy(
                inputText = text,
                outputText = ""
            )
        }
    }

    fun translate() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val translated = translateToEnglishUseCase(_state.value.inputText)
                _state.update { it.copy(outputText = translated) }
            } catch (th: Throwable) {
                _state.update { it.copy(translationError = th.message) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun hideTranslationError() {
        _state.update { it.copy(translationError = null) }
    }
}