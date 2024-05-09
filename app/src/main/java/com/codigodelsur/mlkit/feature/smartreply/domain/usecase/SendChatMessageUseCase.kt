package com.codigodelsur.mlkit.feature.smartreply.domain.usecase

import com.codigodelsur.mlkit.feature.smartreply.domain.repository.ChatRepository
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {

    suspend operator fun invoke(chatId: Int, message: String) =
        repository.sendMessage(chatId = chatId, message = message)
}