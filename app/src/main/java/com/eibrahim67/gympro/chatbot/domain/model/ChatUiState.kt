package com.eibrahim67.gympro.chatbot.domain.model

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isSendButtonVisible: Boolean = false,
    val isRecording: Boolean = false,
    val errorMessage: String? = null
)