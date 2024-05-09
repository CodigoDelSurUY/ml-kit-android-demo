package com.codigodelsur.mlkit.feature.translation.presentation

data class TranslationUIState(
    val inputText: String = "",
    val outputText: String = "",
    val isLoading: Boolean = false,
    val translationError: String? = null
)