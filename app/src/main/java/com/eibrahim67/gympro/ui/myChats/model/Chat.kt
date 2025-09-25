package com.eibrahim67.gympro.ui.myChats.model
import com.google.firebase.Timestamp

data class Chat(
    val chatId: String = "",
    val otherUid: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Timestamp? = null
)