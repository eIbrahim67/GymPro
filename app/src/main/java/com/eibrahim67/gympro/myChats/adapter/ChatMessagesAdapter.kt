package com.eibrahim67.gympro.myChats.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.myChats.ChatMessage
import com.google.android.material.textview.MaterialTextView

class ChatMessagesAdapter(
    private val messages: List<ChatMessage>,
    private val chatWithTrainer: ((id: Int) -> Unit)
) : RecyclerView.Adapter<ChatMessagesAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.item_chat_user_image)
        val userName: MaterialTextView = itemView.findViewById(R.id.chat_user_name)
        val lastMessage: MaterialTextView = itemView.findViewById(R.id.chat_user_last_msg)
        val time: MaterialTextView = itemView.findViewById(R.id.chat_user_last_msg_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false) // change this to your actual XML file name
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = messages[position]
        holder.userName.text = chat.userName
        holder.lastMessage.text = chat.lastMessage
        holder.time.text = chat.time
        holder.userImage.setImageResource(chat.profileImageResId)
        holder.itemView.setOnClickListener {

            chatWithTrainer(chat.userId)

        }
    }

    override fun getItemCount(): Int = messages.size
}
