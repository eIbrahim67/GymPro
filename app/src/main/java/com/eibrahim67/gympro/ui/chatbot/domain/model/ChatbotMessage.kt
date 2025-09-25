package com.eibrahim67.gympro.ui.chatbot.domain.model

data class ChatbotMessage(
    val content: String,
    val role: String = "assistant",
    val isFromUser: Boolean = false
)
