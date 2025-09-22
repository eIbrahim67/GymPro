package com.eibrahim67.gympro.chat.model

data class ChatMessage(
    val senderId: String = "",
    val text: String? = null,
    val imageUrl: String? = null,
    val timestamp: Long = 0L,
    val type: String = "text"
)