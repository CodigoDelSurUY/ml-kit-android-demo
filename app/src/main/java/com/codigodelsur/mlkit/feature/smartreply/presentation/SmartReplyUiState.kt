package com.codigodelsur.mlkit.feature.smartreply.presentation

import com.codigodelsur.mlkit.feature.smartreply.presentation.model.PChatMessage

data class SmartReplyUiState(
    val chatHistory: List<PChatMessage> = emptyList(),
    val inputMessage: String = "",
    val replySuggestions: List<String> = emptyList()
)