package com.eibrahim67.gympro.ui.chat.model

import com.google.firebase.Timestamp

data class ChatMessage(
    val senderId: String = "",
    val text: String? = null,
    val imageUrl: String? = null,
    val timestamp: Timestamp? = null,
    val textMessage: Boolean = true
)