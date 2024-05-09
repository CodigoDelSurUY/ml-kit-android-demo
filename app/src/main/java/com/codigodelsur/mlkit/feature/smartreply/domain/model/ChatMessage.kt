package com.codigodelsur.mlkit.feature.smartreply.domain.model

import java.util.Date

data class ChatMessage(
    val userId: String,
    val date: Date,
    val text: String,
    val isFromMe: Boolean // Just a helper property for this example, could be derived from the userId and current user id in a real app.
)