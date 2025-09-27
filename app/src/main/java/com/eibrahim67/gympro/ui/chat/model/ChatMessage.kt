package com.eibrahim67.gympro.ui.chat.model

import com.google.firebase.Timestamp

data class ChatMessage(
    val msgUid: String = "",
    val text: String? = null,
    val imageUrl: String? = null,
    val msgAudio: Boolean = false,
    val audioUrl: String? = null,
    val timestamp: Timestamp? = null,
)
