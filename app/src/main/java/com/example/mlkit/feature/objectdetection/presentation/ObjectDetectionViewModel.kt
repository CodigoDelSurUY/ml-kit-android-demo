package com.example.mlkit.feature.objectdetection.presentation

import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.objects.DetectedObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ObjectDetectionViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ObjectDetectionUiState())
    val state: StateFlow<ObjectDetectionUiState> = _state.asStateFlow()

    fun updateDetectedObjects(objects: List<DetectedObject>) {
        _state.update {
            it.copy(
               detectedObjects = objects
            )
        }
    }

    fun toggleHideUnlabeled() {
        _state.update {
            it.copy(
                hideUnlabeled = !it.hideUnlabeled
            )
        }
    }
}