package com.eibrahim67.gympro.ui.chatbot.data.repository

import com.eibrahim67.gympro.utils.response.ResponseEI
import com.eibrahim67.gympro.ui.chatbot.domain.model.ChatbotMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChatResponse(jsonPayload: String): Flow<ResponseEI<ChatbotMessage>>
}