package com.example.mlkit.feature.smartreply.domain.usecase

import com.example.mlkit.feature.smartreply.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatHistoryStreamUseCase @Inject constructor(
    private val repository: ChatRepository
) {

    operator fun invoke(chatId: Int) = repository.getChatHistoryStream(chatId = chatId)
}