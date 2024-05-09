package com.example.mlkit.feature.smartreply.presentation

import com.example.mlkit.feature.smartreply.presentation.model.PChatMessage

data class SmartReplyUiState(
    val chatHistory: List<PChatMessage> = emptyList(),
    val inputMessage: String = "",
    val replySuggestions: List<String> = emptyList()
)