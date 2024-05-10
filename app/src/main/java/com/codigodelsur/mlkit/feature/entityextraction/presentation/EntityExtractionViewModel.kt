package com.codigodelsur.mlkit.feature.entityextraction.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codigodelsur.mlkit.feature.entityextraction.domain.usecase.ExtractEntitiesUseCase
import com.codigodelsur.mlkit.feature.entityextraction.presentation.model.PEntityExtractionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntityExtractionViewModel @Inject constructor(
    private val extractEntitiesUseCase: ExtractEntitiesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EntityExtractionUiState())
    val state: StateFlow<EntityExtractionUiState> = _state.asStateFlow()


    fun updateInputText(text: String) {
        _state.update { it.copy(inputText = text) }
    }

    fun extractEntities() {
        val text = _state.value.inputText
        if (text.isNotBlank()) {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                try {
                    val entities = extractEntitiesUseCase(_state.value.inputText)
                    _state.update {
                        it.copy(
                            extractionResult = PEntityExtractionResult(
                                text = text,
                                entityAnnotations = entities
                            )
                        )
                    }
                } catch (th: Throwable) {
                    _state.update { it.copy(extractionError = th.message) }
                } finally {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun hideExtractionError() {
        _state.update { it.copy(extractionError = null) }
    }
}
