package com.eibrahim67.gympro.chatbot.domain.repositoryImpl

import com.eibrahim67.gympro.chatbot.data.network.ChatLlamaStreamProcessor
import com.eibrahim67.gympro.chatbot.data.repository.ChatRepository
import com.eibrahim67.gympro.chatbot.domain.model.ChatMessage
import com.eibrahim67.gympro.core.response.FailureReason
import com.eibrahim67.gympro.core.response.ResponseEI
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ChatRepositoryImpl(
    private val streamProcessor: ChatLlamaStreamProcessor
) : ChatRepository {
    override suspend fun getChatResponse(jsonPayload: String): Flow<ResponseEI<ChatMessage>> = callbackFlow {
        // Emit a loading state immediately.
        trySend(ResponseEI.Loading)

        val conversationBuilder = StringBuilder()
        streamProcessor.getChatLlamaStream(
            jsonPayload = jsonPayload,
            onMessageReceived = { line ->
                conversationBuilder.append(line).append("\n")
                // Optionally, emit intermediate loading states or partial updates.
                trySend(ResponseEI.Loading)
            },
            onError = { e ->
                trySend(ResponseEI.Failure(FailureReason.UnknownError(e.toString())))
                close(e)
            },
            onReceiving = {
                // Could update UI to show shimmer/loading.
                trySend(ResponseEI.Loading)
            },
            onComplete = {
                trySend(ResponseEI.Success(ChatMessage(content = conversationBuilder.toString())))
                close()
            }
        )

        awaitClose { /* Optionally cancel the stream if needed */ }
    }
}