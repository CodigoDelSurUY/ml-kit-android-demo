package com.codigodelsur.mlkit.feature.posedetection.presentation

import androidx.lifecycle.ViewModel
import com.codigodelsur.mlkit.feature.posedetection.presentation.classification.PoseClassificationResult
import com.codigodelsur.mlkit.feature.posedetection.presentation.model.PPose
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PoseDetectionViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(PoseDetectionUiState())
    val state: StateFlow<PoseDetectionUiState> = _state.asStateFlow()

    fun updateDetectedPose(
        pose: PPose?,
        classification: PoseClassificationResult?,
        inputImageWidth: Int,
        inputImageHeight: Int
    ) {
        _state.update {
            it.copy(
                detectedPose = pose,
                reps = classification?.reps ?: 0,
                className = classification?.className,
                inputImageWidth = inputImageWidth,
                inputImageHeight = inputImageHeight
            )
        }
    }
}