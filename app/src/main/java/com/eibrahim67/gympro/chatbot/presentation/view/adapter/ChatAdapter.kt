package com.eibrahim67.gympro.chatbot.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eibrahim67.gympro.chatbot.domain.model.ChatMessage
import com.eibrahim67.gympro.databinding.ItemChatBotBinding
import com.eibrahim67.gympro.databinding.ItemChatUserBinding
import io.noties.markwon.Markwon


/**
 * Adapter for displaying chatbot conversation messages and associated property images.
 */
class ChatAdapter : ListAdapter<ChatAdapter.ChatItem, ChatAdapter.ChatViewHolder>(ChatItemDiffCallback()) {

    /**
     * Sealed class representing different types of chat items.
     */
    sealed class ChatItem {
        data class UserMessage(val message: ChatMessage) : ChatItem()
        data class BotMessage(val message: ChatMessage) : ChatItem()
    }

    /**
     * ViewHolder for chat messages, using View Binding for efficient view access.
     */
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

    /**
     * Updates the adapter with new messages and properties.
     */
    fun updateData(messages: List<ChatMessage>) {
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

/**
 * DiffUtil callback for efficient updates of chat items.
 */
private class ChatItemDiffCallback : DiffUtil.ItemCallback<ChatAdapter.ChatItem>() {
    override fun areItemsTheSame(
        oldItem: ChatAdapter.ChatItem,
        newItem: ChatAdapter.ChatItem
    ): Boolean {
        return when {
            oldItem is ChatAdapter.ChatItem.UserMessage && newItem is ChatAdapter.ChatItem.UserMessage ->
                oldItem.message.content == newItem.message.content

            oldItem is ChatAdapter.ChatItem.BotMessage && newItem is ChatAdapter.ChatItem.BotMessage ->
                oldItem.message.content == newItem.message.content

            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: ChatAdapter.ChatItem,
        newItem: ChatAdapter.ChatItem
    ): Boolean {
        return oldItem == newItem
    }
}