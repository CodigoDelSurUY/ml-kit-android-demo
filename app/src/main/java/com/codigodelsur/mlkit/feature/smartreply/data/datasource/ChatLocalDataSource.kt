package com.codigodelsur.mlkit.feature.smartreply.data.datasource

import android.icu.util.Calendar
import com.codigodelsur.mlkit.feature.smartreply.domain.model.ChatMessage
import com.google.mlkit.nl.smartreply.SmartReplyGenerator
import com.google.mlkit.nl.smartreply.TextMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface ChatLocalDataSource {
    fun getChatHistoryStream(chatId: Int): Flow<List<ChatMessage>>
    suspend fun sendMessage(chatId: Int, message: String)

    suspend fun getReplySuggestions(chatHistory: List<ChatMessage>): List<String>
}

class ChatLocalDataSourceImpl @Inject constructor(
    private val smartReplyGenerator: SmartReplyGenerator
) : ChatLocalDataSource {
    // Simulate a local database
    private val _chatHistory: MutableStateFlow<List<ChatMessage>> = MutableStateFlow(listOf())

    init {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, -1)
        val messages = listOf(
            "Hello! How are you?",
            "Fine and you?",
            "All good, hey listen...would you like to go to the bar?",
            "I don't know man, I should study...",
            "But you only live once! Cmon dude can I count on you today?"
        )
        val history = messages.mapIndexed { index, message ->
            calendar.add(Calendar.MINUTE, index)
            val isFromMe = index % 2 == 1
            ChatMessage(
                text = message,
                isFromMe = isFromMe,
                date = calendar.time,
                userId = if (isFromMe) "1" else "2"
            )
        }
        _chatHistory.update { history.toList() }
    }

    override fun getChatHistoryStream(chatId: Int): Flow<List<ChatMessage>> =
        _chatHistory.asStateFlow()

    override suspend fun sendMessage(chatId: Int, message: String) {
        val chats = _chatHistory.value.toMutableList()
        chats.add(ChatMessage(text = message, isFromMe = true, date = Date(), userId = "1"))
        _chatHistory.update { chats.toList() }
    }

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