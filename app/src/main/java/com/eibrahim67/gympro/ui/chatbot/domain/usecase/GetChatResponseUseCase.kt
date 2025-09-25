package com.eibrahim67.gympro.ui.chatbot.domain.usecase

import com.eibrahim67.gympro.utils.response.ResponseEI
import com.eibrahim67.gympro.ui.chatbot.data.repository.ChatRepository
import com.eibrahim67.gympro.ui.chatbot.domain.model.ChatbotMessage
import kotlinx.coroutines.flow.Flow

class GetChatResponseUseCase(private val repository: ChatRepository) {
    suspend fun execute(jsonPayload: String): Flow<ResponseEI<ChatbotMessage>> {
        // You could add validation or other business rules here.
        return repository.getChatResponse(jsonPayload)
    }
}