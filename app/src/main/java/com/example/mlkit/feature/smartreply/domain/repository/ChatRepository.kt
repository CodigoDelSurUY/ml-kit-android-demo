package com.example.mlkit.feature.smartreply.domain.repository

import com.example.mlkit.feature.smartreply.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatHistoryStream(chatId: Int): Flow<List<ChatMessage>>
    suspend fun sendMessage(chatId: Int, message: String)
    suspend fun getReplySuggestions(chatHistory: List<ChatMessage>): List<String>
}