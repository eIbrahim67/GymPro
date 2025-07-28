package com.eibrahim67.gympro.chatbot.data.repository

import com.eibrahim67.gympro.chatbot.domain.model.ChatMessage
import com.eibrahim67.gympro.core.response.ResponseEI
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChatResponse(jsonPayload: String): Flow<ResponseEI<ChatMessage>>
}