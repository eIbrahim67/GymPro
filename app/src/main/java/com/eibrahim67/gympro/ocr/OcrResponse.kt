package com.eibrahim67.gympro.ocr

data class OcrResponse(
    val status: String,
    val extractedText: String?,
    val error: String?
)