package com.eibrahim67.gympro.ui.chatbot.domain.model

data class ChatUiState(
    val messages: List<ChatbotMessage> = emptyList(),
    val isSendButtonVisible: Boolean = false,
    val isRecording: Boolean = false,
    val errorMessage: String? = null
)