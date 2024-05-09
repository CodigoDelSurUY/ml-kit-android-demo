package com.codigodelsur.mlkit.feature.smartreply.presentation.model

import com.codigodelsur.mlkit.feature.smartreply.domain.model.ChatMessage
import java.util.Date

data class PChatMessage(
    val text: String,
    val isFromMe: Boolean,
    val date: Date
)

fun ChatMessage.toPresentation() = PChatMessage(
    text = text,
    isFromMe = isFromMe,
    date = date
)