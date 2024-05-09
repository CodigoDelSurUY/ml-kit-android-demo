package com.codigodelsur.mlkit.feature.smartreply.data.repository

import com.codigodelsur.mlkit.feature.smartreply.data.datasource.ChatLocalDataSource
import com.codigodelsur.mlkit.feature.smartreply.data.datasource.ChatRemoteDataSource
import com.codigodelsur.mlkit.feature.smartreply.domain.model.ChatMessage
import com.codigodelsur.mlkit.feature.smartreply.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatLocalDataSource: ChatLocalDataSource,
    private val chatRemoteDataSource: ChatRemoteDataSource
) : ChatRepository {

    override fun getChatHistoryStream(chatId: Int): Flow<List<ChatMessage>> =
        chatLocalDataSource.getChatHistoryStream(chatId = chatId)

    override suspend fun sendMessage(chatId: Int, message: String) {
        chatLocalDataSource.sendMessage(chatId = chatId, message = message)
    }

    override suspend fun getReplySuggestions(chatHistory: List<ChatMessage>): List<String> =
        chatRemoteDataSource.getReplySuggestions(chatHistory = chatHistory)
}