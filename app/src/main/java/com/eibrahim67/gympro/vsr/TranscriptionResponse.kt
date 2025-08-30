package com.eibrahim67.gympro.vsr

data class TranscriptionResponse(
    val status: String,
    val transcribedText: String?,
    val error: String?
)