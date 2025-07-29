package com.eibrahim67.gympro.myChats

data class ChatMessage(
    val userId: Int,
    val userName: String,
    val lastMessage: String,
    val time: String,
    val profileImageResId: Int // or use a URL if you're loading images from the network
)
