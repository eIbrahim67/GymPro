package com.eibrahim67.gympro.chat.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.chat.model.ChatMessage

class ChatAdapter(private val currentUserId: String?) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {
    private val messages = mutableListOf<ChatMessage>()

    inner class MessageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val senderMessage: TextView = view.findViewById(R.id.senderMessage)
        val receiverMessage: TextView = view.findViewById(R.id.receiverMessage)

        val receiverMessageCard: LinearLayout = view.findViewById(R.id.receiverMessageCard)
        val senderMessageCard: LinearLayout = view.findViewById(R.id.senderMessageCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_users_chat, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val msg = messages[position]
        val isCurrentUser = msg.senderId == currentUserId

        if (isCurrentUser) {
            holder.receiverMessageCard.visibility = View.GONE
            holder.senderMessageCard.visibility = View.VISIBLE
            holder.senderMessage.text = msg.text

        } else {
            holder.senderMessageCard.visibility = View.GONE
            holder.receiverMessageCard.visibility = View.VISIBLE
            holder.receiverMessage.text = msg.text
        }
    }

    override fun getItemCount() = messages.size

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearMessages() {
        messages.clear()
        notifyDataSetChanged()
    }
}