package com.eibrahim67.gympro.chatbot.ocr

data class OcrResponse(
    val status: String,
    val extractedText: String?,
    val error: String?
)