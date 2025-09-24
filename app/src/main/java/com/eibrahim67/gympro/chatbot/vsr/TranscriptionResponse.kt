package com.eibrahim67.gympro.chatbot.vsr

data class TranscriptionResponse(
    val status: String,
    val transcribedText: String?,
    val error: String?
)