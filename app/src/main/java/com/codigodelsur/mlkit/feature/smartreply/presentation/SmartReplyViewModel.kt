package com.codigodelsur.mlkit.feature.smartreply.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codigodelsur.mlkit.feature.smartreply.domain.model.ChatMessage
import com.codigodelsur.mlkit.feature.smartreply.domain.usecase.GetChatHistoryStreamUseCase
import com.codigodelsur.mlkit.feature.smartreply.domain.usecase.GetReplySuggestionsUseCase
import com.codigodelsur.mlkit.feature.smartreply.domain.usecase.SendChatMessageUseCase
import com.codigodelsur.mlkit.feature.smartreply.presentation.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmartReplyViewModel @Inject constructor(
    private val getChatHistoryStreamUseCase: GetChatHistoryStreamUseCase,
    private val getReplySuggestionsUseCase: GetReplySuggestionsUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase
) : ViewModel() {
    // Simulate we are looking at a specific chat.
    private val chatId = 1

    private var suggestionsJob: Job? = null

    private val _state = MutableStateFlow(SmartReplyUiState())
    val state: StateFlow<SmartReplyUiState> = _state.asStateFlow()


    init {
        viewModelScope.launch {
            getChatHistoryStreamUseCase(chatId = chatId)
                .collectLatest { history ->
                    getReplySuggestions(chatHistory = history)
                    _state.update {
                        it.copy(chatHistory = history.map { message -> message.toPresentation() })
                    }
                }
        }
    }

    private fun getReplySuggestions(chatHistory: List<ChatMessage>) {
        suggestionsJob?.cancel()
        suggestionsJob = viewModelScope.launch {
            // In a real application, you shouldn't fetch suggestions if the last message is yours.
            // For the purposes of this example, lets just fetch them anyways.
            try {
                val suggestions = getReplySuggestionsUseCase(chatHistory = chatHistory)
                _state.update { it.copy(replySuggestions = suggestions) }
            } catch (th: Throwable) {
                if (th !is CancellationException) {
                    Log.e(TAG, "Unable to fetch suggestions", th)
                    _state.update { it.copy(replySuggestions = emptyList()) }
                }
            }
        }
    }

    private fun sendMessage(message: String) {
        if (message.isNotBlank()) {
            viewModelScope.launch {
                try {
                    sendChatMessageUseCase(chatId = chatId, message = message)
                    _state.update { it.copy(inputMessage = "") }
                } catch (th: Throwable) {
                    Log.e(TAG, "Unable to send message", th)
                }
            }
        }
    }

    fun updateInputMessage(message: String) {
        _state.update { it.copy(inputMessage = message) }
    }

    fun send() {
        val message = _state.value.inputMessage
        sendMessage(message = message)
    }

    fun sendSuggestion(suggestion: String) {
        sendMessage(message = suggestion)
    }

    companion object {
        private const val TAG = "SmartReplyViewModel"
    }
}