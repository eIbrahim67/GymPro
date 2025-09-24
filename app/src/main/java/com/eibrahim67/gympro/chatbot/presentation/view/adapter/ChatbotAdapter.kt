package com.eibrahim67.gympro.chatbot.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eibrahim67.gympro.chatbot.domain.model.ChatbotMessage
import com.eibrahim67.gympro.databinding.ItemChatBotBinding
import com.eibrahim67.gympro.databinding.ItemChatUserBinding
import io.noties.markwon.Markwon

class ChatbotAdapter : ListAdapter<ChatbotAdapter.ChatItem, ChatbotAdapter.ChatViewHolder>(ChatItemDiffCallback()) {

    sealed class ChatItem {
        data class UserMessage(val message: ChatbotMessage) : ChatItem()
        data class BotMessage(val message: ChatbotMessage) : ChatItem()
    }

    sealed class ChatViewHolder : RecyclerView.ViewHolder {
        constructor(binding: ItemChatUserBinding) : super(binding.root)
        constructor(binding: ItemChatBotBinding) : super(binding.root)

        abstract fun bind(item: ChatItem)
    }

    private class UserViewHolder(
        private val binding: ItemChatUserBinding,
        private val markwon: Markwon
    ) : ChatViewHolder(binding) {
        override fun bind(item: ChatItem) {
            if (item is ChatItem.UserMessage) {
                markwon.setMarkdown(binding.messageTextView, item.message.content)
            }
        }
    }

    private class BotViewHolder(
        private val binding: ItemChatBotBinding,
        private val markwon: Markwon,
    ) : ChatViewHolder(binding) {
        override fun bind(item: ChatItem) {
            if (item is ChatItem.BotMessage) {
                markwon.setMarkdown(binding.messageTextView, item.message.content)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val markwon = Markwon.create(parent.context)
        return when (viewType) {
            USER_MESSAGE -> UserViewHolder(
                ItemChatUserBinding.inflate(inflater, parent, false),
                markwon
            )

            BOT_MESSAGE -> BotViewHolder(
                ItemChatBotBinding.inflate(inflater, parent, false),
                markwon
            )

            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is ChatItem.UserMessage -> USER_MESSAGE
        is ChatItem.BotMessage -> BOT_MESSAGE
    }

    fun updateData(messages: List<ChatbotMessage>) {
        val items = messages.map { message ->
            if (message.isFromUser) {
                ChatItem.UserMessage(message)
            } else {
                ChatItem.BotMessage(message)
            }
        }
        submitList(items)
    }

    companion object {
        private const val USER_MESSAGE = 0
        private const val BOT_MESSAGE = 1
    }
}

private class ChatItemDiffCallback : DiffUtil.ItemCallback<ChatbotAdapter.ChatItem>() {
    override fun areItemsTheSame(
        oldItem: ChatbotAdapter.ChatItem,
        newItem: ChatbotAdapter.ChatItem
    ): Boolean {
        return when {
            oldItem is ChatbotAdapter.ChatItem.UserMessage && newItem is ChatbotAdapter.ChatItem.UserMessage ->
                oldItem.message.content == newItem.message.content

            oldItem is ChatbotAdapter.ChatItem.BotMessage && newItem is ChatbotAdapter.ChatItem.BotMessage ->
                oldItem.message.content == newItem.message.content

            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: ChatbotAdapter.ChatItem,
        newItem: ChatbotAdapter.ChatItem
    ): Boolean {
        return oldItem == newItem
    }
}