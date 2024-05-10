package com.codigodelsur.mlkit.feature.entityextraction.presentation

import com.codigodelsur.mlkit.feature.entityextraction.presentation.model.PEntityExtractionResult

data class EntityExtractionUiState(
    val inputText: String = "",
    val isLoading: Boolean = false,
    val extractionResult: PEntityExtractionResult? = null,
    val extractionError: String? = null
)