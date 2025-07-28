package com.eibrahim67.gympro.ocr

data class OcrResponse(
    val status: String,
    val extracted_text: String?,
    val error: String?
)