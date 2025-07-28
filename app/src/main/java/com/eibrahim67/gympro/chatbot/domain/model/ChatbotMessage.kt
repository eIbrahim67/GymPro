package com.eibrahim67.gympro.chatbot.domain.model

data class ChatbotMessage(
    val content: String,
    val role: String = "assistant",
    val isFromUser: Boolean = false
)
