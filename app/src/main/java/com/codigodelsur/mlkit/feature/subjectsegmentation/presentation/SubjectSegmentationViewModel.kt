package com.codigodelsur.mlkit.feature.subjectsegmentation.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SubjectSegmentationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(SubjectSegmentationUIState())
    val state: StateFlow<SubjectSegmentationUIState> = _state.asStateFlow()

    fun showEditorError(throwable: Throwable) {
        _state.update { it.copy(editorError = throwable.message) }
    }

    fun hideEditorError() {
        _state.update { it.copy(editorError = null) }
    }
}