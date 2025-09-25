package com.eibrahim67.gympro.ui.chat.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eibrahim67.gympro.ui.chat.model.ChatMessage
import com.eibrahim67.gympro.ui.chat.model.UserChat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlin.toString

class ChatViewModel : ViewModel() {


    private val _chatId = MutableLiveData<String>()
    val chatId: LiveData<String> get() = _chatId
    private fun getChatId(user1: String, user2: String): String {
        _chatId.value = if (user1 < user2) "${user1}_$user2" else "${user2}_$user1"
        return chatId.value.toString()
    }

    fun createOrGetChat(currentUid: String, otherUid: String, onChatId: (String) -> Unit) {
        val chatId = getChatId(currentUid, otherUid)
        val db = FirebaseFirestore.getInstance()
        val chatRef = db.collection("chats").document(chatId)
        val userChatsRef = db.collection("userChats").document(currentUid)

        chatRef.get().addOnSuccessListener { doc ->
            if (!doc.exists()) {
                val chatData = hashMapOf(
                    "members" to listOf(currentUid, otherUid),
                    "createdAt" to FieldValue.serverTimestamp()
                )
                chatRef.set(chatData)

                userChatsRef.set(
                    mapOf("chats" to FieldValue.arrayUnion(chatId)), SetOptions.merge()
                )

            }
            onChatId(chatId)
        }
    }


    fun sendMessage(chatId: String, text: String, imageUri: String?) {
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().uid ?: return

        val message = hashMapOf(
            "senderId" to uid,
            "text" to text, // empty for images
            "textMessage" to text.isNotEmpty(),
            "imageUrl" to imageUri,
            "timestamp" to FieldValue.serverTimestamp()
        )

        val chatRef = db.collection("chats").document(chatId)

        db.runBatch { batch ->
            val msgRef = chatRef.collection("messages").document()
            batch.set(msgRef, message)
            batch.update(
                chatRef, mapOf(
                    "lastMessage" to text, "lastMessageTime" to FieldValue.serverTimestamp()
                )
            )
        }

    }

    fun listenForMessages(chatId: String, onMessage: (ChatMessage) -> Unit) {
        if (chatId.isBlank()) {
            Log.e("ChatsViewModel", "listenForMessages: chatId is empty!")
            return
        }

        FirebaseFirestore.getInstance().collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp").addSnapshotListener { snapshots, e ->
                if (e != null || snapshots == null) return@addSnapshotListener

                for (docChange in snapshots.documentChanges) {
                    if (docChange.type == DocumentChange.Type.ADDED) {
                        val message = docChange.document.toObject(ChatMessage::class.java)
                        onMessage(message)
                    }
                }
            }
    }

    fun getUserInfo(uid: String?, onUser: (UserChat?) -> Unit) {

        if (uid.isNullOrEmpty()) {
            onUser(null)
            return
        }

        FirebaseFirestore.getInstance().collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val user = UserChat(
                        uid,
                        doc.getString("name") ?: "Unknown",
                        doc.getString("email") ?: "",
                        doc.getString("photoUrl")
                    )
                    onUser(user)
                }
            }
    }

}