package com.codigodelsur.mlkit.feature.selfiesegmentation.presentation

import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.segmentation.SegmentationMask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SelfieSegmentationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(SelfieSegmentationUiState())
    val state: StateFlow<SelfieSegmentationUiState> = _state.asStateFlow()

    fun updateSegmentedMask(mask: SegmentationMask?) {
        _state.update { it.copy(selfieMask = mask) }
    }
}