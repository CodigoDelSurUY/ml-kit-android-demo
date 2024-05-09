package com.example.mlkit.feature.smartreply.data.datasource

import com.example.mlkit.feature.smartreply.domain.model.ChatMessage
import com.google.mlkit.nl.smartreply.SmartReplyGenerator
import com.google.mlkit.nl.smartreply.TextMessage
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface ChatRemoteDataSource {
    suspend fun getReplySuggestions(chatHistory: List<ChatMessage>): List<String>
}

class ChatRemoteDataSourceImpl @Inject constructor(
    private val smartReplyGenerator: SmartReplyGenerator
) : ChatRemoteDataSource {

    override suspend fun getReplySuggestions(chatHistory: List<ChatMessage>): List<String> =
        suspendCoroutine { continuation ->

            val conversation = chatHistory.takeLast(10).map { message ->
                with(message) {
                    if (isFromMe) {
                        TextMessage.createForLocalUser(text, date.time)
                    } else {
                        TextMessage.createForRemoteUser(
                            text,
                            date.time,
                            userId
                        )
                    }
                }
            }
            smartReplyGenerator.suggestReplies(conversation)
                .addOnSuccessListener { result ->
                    val suggestions = result.suggestions.map { it.text }
                    continuation.resume(suggestions)
                }
                .addOnFailureListener(continuation::resumeWithException)
        }
}