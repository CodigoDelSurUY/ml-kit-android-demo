package com.codigodelsur.mlkit.feature.smartreply.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.component.MlkTopAppBar
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme
import com.codigodelsur.mlkit.feature.smartreply.presentation.component.ChatInput
import com.codigodelsur.mlkit.feature.smartreply.presentation.component.ChatMessageItem
import com.codigodelsur.mlkit.feature.smartreply.presentation.component.ChatReplySuggestions
import com.codigodelsur.mlkit.feature.smartreply.presentation.model.PChatMessage
import java.util.Date

@Composable
fun SmartReplyRoute(
    modifier: Modifier = Modifier,
    viewModel: SmartReplyViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SmartReplyScreen(
        modifier = modifier,
        chatHistory = state.chatHistory,
        replySuggestions = state.replySuggestions,
        inputMessage = state.inputMessage,
        onSuggestionClick = viewModel::sendSuggestion,
        onInputChange = viewModel::updateInputMessage,
        onSendClick = viewModel::send,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SmartReplyScreen(
    modifier: Modifier = Modifier,
    chatHistory: List<PChatMessage>,
    replySuggestions: List<String>,
    inputMessage: String,
    onSuggestionClick: (String) -> Unit,
    onInputChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val listState = rememberLazyListState()
    LaunchedEffect(key1 = chatHistory.size) {
        listState.animateScrollToItem(index = chatHistory.lastIndex)
    }
    Column(modifier = modifier.fillMaxSize()) {
        MlkTopAppBar(
            titleRes = R.string.feature_smart_reply_title,
            onNavigationClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(all = 16.dp),
            state = listState
        ) {
            items(chatHistory) {
                ChatMessageItem(
                    modifier = Modifier.fillMaxWidth(),
                    message = it
                )
            }
        }

        if (replySuggestions.isNotEmpty()) {
            ChatReplySuggestions(
                modifier = Modifier.fillMaxWidth(),
                suggestions = replySuggestions,
                onSuggestionClick = onSuggestionClick
            )
        }

        ChatInput(
            modifier = Modifier.fillMaxWidth(),
            inputMessage = inputMessage,
            onInputChange = onInputChange,
            onSendClick = onSendClick
        )
    }
}

@Preview
@Composable
private fun SmartReplyScreenPreview() {
    MlkTheme {
        SmartReplyScreen(
            chatHistory = listOf(
                PChatMessage("Hello!", isFromMe = true, date = Date()),
                PChatMessage("Hi there", isFromMe = false, date = Date())
            ),
            replySuggestions = listOf("Ok", "No"),
            inputMessage = "",
            onSuggestionClick = {},
            onInputChange = {},
            onSendClick = {},
            onBackClick = {}
        )
    }
}