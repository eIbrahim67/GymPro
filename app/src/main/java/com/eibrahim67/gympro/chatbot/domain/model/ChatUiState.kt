package com.eibrahim67.gympro.chatbot.domain.model

/**
 * Data class representing the UI state for the chatbot.
 */
data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isSendButtonVisible: Boolean = false,
    val isRecording: Boolean = false,
    val errorMessage: String? = null
)