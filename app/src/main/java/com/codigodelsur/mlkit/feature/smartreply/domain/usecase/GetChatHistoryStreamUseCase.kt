package com.codigodelsur.mlkit.feature.smartreply.domain.usecase

import com.codigodelsur.mlkit.feature.smartreply.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatHistoryStreamUseCase @Inject constructor(
    private val repository: ChatRepository
) {

    operator fun invoke(chatId: Int) = repository.getChatHistoryStream(chatId = chatId)
}