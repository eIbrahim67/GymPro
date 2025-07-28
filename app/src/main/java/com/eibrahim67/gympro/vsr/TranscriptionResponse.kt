package com.eibrahim67.gympro.vsr

data class TranscriptionResponse(
    val status: String,
    val transcribed_text: String?,
    val error: String?
)