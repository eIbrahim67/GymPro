package com.eibrahim67.gympro.myChats.view.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.myChats.model.Chat
import com.eibrahim67.gympro.myChats.model.UserChat


class ChatsAdapter(
    private val onChatClick: (Chat, UserChat) -> Unit
) : RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>() {

    private val chats = mutableListOf<Pair<Chat, UserChat>>()

    fun updateChats(newChat: Pair<Chat, UserChat>) {
        chats.removeAll { (_, userChat) ->
            userChat.uid == newChat.second.uid   // compare, not assign
        }
        chats.add(newChat)

        chats.sortByDescending { it.first.lastMessageTime }

        notifyDataSetChanged()
    }

    inner class ChatViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(chat: Chat, user: UserChat) {

            val formattedRelative: String = chat.lastMessageTime?.toDate()?.let { date ->
                    DateUtils.getRelativeTimeSpanString(
                        date.time, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS
                    ).toString()
                } ?: ""


            view.findViewById<TextView>(R.id.chat_user_name).text = user.name
            view.findViewById<TextView>(R.id.chat_user_last_msg).text = chat.lastMessage
            view.findViewById<TextView>(R.id.chat_user_last_msg_time).text = formattedRelative
            Glide.with(view.context).load(user.photoUrl).error(R.drawable.icon_solid_profile)
                .into(view.findViewById<ImageView>(R.id.item_chat_user_image))
            view.setOnClickListener { onChatClick(chat, user) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val (chat, user) = chats[position]
        holder.bind(chat, user)
    }


    override fun getItemCount(): Int = chats.size
}
