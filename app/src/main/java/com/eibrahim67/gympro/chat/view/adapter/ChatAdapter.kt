package com.eibrahim67.gympro.chat.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.chat.model.ChatMessage

class ChatAdapter(private val currentUserId: String?) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {
    private val messages = mutableListOf<ChatMessage>()

    inner class MessageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val senderMessage: TextView = view.findViewById(R.id.senderMessage)
        val receiverMessage: TextView = view.findViewById(R.id.receiverMessage)

        val senderImage: ImageView = view.findViewById(R.id.senderImage)
        val senderImageCard: LinearLayout = view.findViewById(R.id.senderImageCard)

        val receiverMessageCard: LinearLayout = view.findViewById(R.id.receiverMessageCard)
        val senderMessageCard: LinearLayout = view.findViewById(R.id.senderMessageCard)

        val receiverImage: ImageView = view.findViewById(R.id.receiverImage)
        val receiverImageCard: LinearLayout = view.findViewById(R.id.receiverImageCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_users_chat, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val msg = messages[position]
        val isCurrentUser = msg.senderId == currentUserId

        Log.d("ChatAdapter", "Got something")

        if (isCurrentUser) {
            if (msg.textMessage) {
                // current user, text message
                Log.d("ChatAdapter", "Case: Current user sent TEXT")

                holder.senderMessageCard.show()
                holder.senderMessage.text = msg.text

                holder.receiverMessageCard.hide()
                holder.senderImageCard.hide()
                holder.receiverImageCard.hide()
            } else {
                // current user, image message
                Log.d("ChatAdapter", "Case: Current user sent IMAGE")
                Log.d("ChatAdapter", msg.imageUrl.toString())

                holder.senderImageCard.show()
                Glide.with(holder.view.context)
                    .load(msg.imageUrl)
                    .placeholder(R.color.gray_v1)
                    .error(R.drawable.error_ic)
                    .centerCrop()
                    .into(holder.senderImage)

                holder.senderMessageCard.hide()
                holder.receiverMessageCard.hide()
                holder.receiverImageCard.hide()
            }
        } else {
            if (msg.textMessage) {
                // other user, text message
                Log.d("ChatAdapter", "Case: Other user sent TEXT")

                holder.receiverMessageCard.show()
                holder.receiverMessage.text = msg.text

                holder.senderMessageCard.hide()
                holder.senderImageCard.hide()
                holder.receiverImageCard.hide()
            } else {
                // other user, image message
                Log.d("ChatAdapter", "Case: Other user sent IMAGE")

                holder.receiverImageCard.show()
                Glide.with(holder.view.context)
                    .load(msg.imageUrl)
                    .placeholder(R.color.primary_white)
                    .error(R.drawable.error_ic)
                    .centerCrop()
                    .into(holder.receiverImage)

                holder.senderMessageCard.hide()
                holder.senderImageCard.hide()
                holder.receiverMessageCard.hide()
            }
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

    fun View.show() { visibility = View.VISIBLE }
    fun View.hide() { visibility = View.GONE }


}