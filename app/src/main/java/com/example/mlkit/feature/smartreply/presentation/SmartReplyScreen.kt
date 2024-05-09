package com.example.mlkit.feature.smartreply.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mlkit.R
import com.example.mlkit.core.presentation.component.MlkTopAppBar
import com.example.mlkit.feature.smartreply.presentation.component.ChatInput
import com.example.mlkit.feature.smartreply.presentation.component.ChatMessageItem
import com.example.mlkit.feature.smartreply.presentation.component.ChatReplySuggestions
import com.example.mlkit.feature.smartreply.presentation.model.PChatMessage

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
        onSuggestionClicked = viewModel::sendSuggestion,
        onInputChanged = viewModel::updateInputMessage,
        onSendClicked = viewModel::send,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SmartReplyScreen(
    modifier: Modifier,
    chatHistory: List<PChatMessage>,
    replySuggestions: List<String>,
    inputMessage: String,
    onSuggestionClicked: (String) -> Unit,
    onInputChanged: (String) -> Unit,
    onSendClicked: () -> Unit,
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
                suggestions = replySuggestions,
                onSuggestionClicked = onSuggestionClicked
            )
        }
        
        ChatInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            inputMessage = inputMessage,
            onInputChanged = onInputChanged,
            onSendClicked = onSendClicked
        )
    }
}