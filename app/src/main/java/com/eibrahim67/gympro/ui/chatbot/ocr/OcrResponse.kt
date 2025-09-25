package com.eibrahim67.gympro.ui.chatbot.ocr

data class OcrResponse(
    val status: String,
    val extractedText: String?,
    val error: String?
)