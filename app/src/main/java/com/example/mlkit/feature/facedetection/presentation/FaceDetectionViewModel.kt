package com.example.mlkit.feature.facedetection.presentation

import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.face.Face
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FaceDetectionViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(FaceDetectionUiState())
    val state: StateFlow<FaceDetectionUiState> = _state.asStateFlow()

    fun updateDetectedFaces(faces: List<Face>) {
        _state.update {
            it.copy(
                detectedFaces = faces,
                isSmiling = faces.any { face -> (face.smilingProbability ?: 0.0f) > 0.2f }
            )
        }
    }

    fun toggleHat() {
        _state.update { it.copy(showHat = !it.showHat) }
    }

    fun toggleBoundingBoxes() {
        _state.update { it.copy(showBoundingBoxes = !it.showBoundingBoxes) }
    }
}