package com.codigodelsur.mlkit.feature.smartreply.domain.usecase

import com.codigodelsur.mlkit.feature.smartreply.domain.model.ChatMessage
import com.codigodelsur.mlkit.feature.smartreply.domain.repository.ChatRepository
import javax.inject.Inject

class GetReplySuggestionsUseCase @Inject constructor(
    private val repository: ChatRepository
) {

    suspend operator fun invoke(chatHistory: List<ChatMessage>) =
        repository.getReplySuggestions(chatHistory = chatHistory)
}