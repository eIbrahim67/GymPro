package com.eibrahim67.gympro.ui.chat.viewModel

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

class ChatViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _chatId = MutableLiveData<String>()
    val chatId: LiveData<String> get() = _chatId
    fun getChatId(user1: String, user2: String) {
        _chatId.value = if (user1 < user2) "${user1}_$user2" else "${user2}_$user1"
    }

    fun createOrGetChat(
        currentUid: String,
        otherUid: String,
        chatId: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val chatRef = db.collection("chats").document(chatId)
        val currentUserChatsRef = db.collection("userChats").document(currentUid)
        val otherUserChatsRef = db.collection("userChats").document(otherUid)

        chatRef.get().addOnSuccessListener { doc ->
            if (!doc.exists()) {
                val chatData = hashMapOf(
                    "members" to listOf(currentUid, otherUid),
                    "createdAt" to FieldValue.serverTimestamp()
                )
                chatRef.set(chatData)
                currentUserChatsRef.set(
                    mapOf("chats" to FieldValue.arrayUnion(chatId)), SetOptions.merge()
                )
                otherUserChatsRef.set(
                    mapOf("chats" to FieldValue.arrayUnion(chatId)), SetOptions.merge()
                )
            }
        }.addOnSuccessListener { onSuccess() }.addOnFailureListener { onFailure() }
    }

    fun deleteOurChat(onSuccess: () -> Unit, onFailure: () -> Unit) {

        var currentChatId = chatId.value.toString()
        if (currentChatId.isEmpty()) return

        db.collection("chats").document(currentChatId).delete().addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onFailure()
        }
    }

    fun sendMessage(
        text: String, imageUri: String?, isAudio: Boolean, audioUrl: String?
    ) {

        var currentChatId = chatId.value.toString()
        if (currentChatId.isEmpty()) return

        val msgUid = FirebaseAuth.getInstance().uid ?: return

        val message = hashMapOf(
            "msgUid" to msgUid,
            "text" to text,
            "imageUrl" to imageUri,
            "msgAudio" to isAudio,
            "audioUrl" to audioUrl,
            "timestamp" to FieldValue.serverTimestamp()
        )

        val chatRef = db.collection("chats").document(currentChatId)

        db.runBatch { batch ->
            val msgRef = chatRef.collection("messages").document()
            batch.set(msgRef, message)
            batch.update(
                chatRef, mapOf(
                    "lastMessage" to if (text.isEmpty() && isAudio) "Audio \uD83C\uDF99\uFE0F" else if (text.isEmpty()) "Image \uD83D\uDCF8" else text,
                    "lastMessageTime" to FieldValue.serverTimestamp()
                )
            )
        }.addOnSuccessListener {

        }.addOnFailureListener {

        }
    }

    fun listenForMessages(chatId: String, onMessage: (ChatMessage) -> Unit) {
        if (chatId.isBlank()) return

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

        db.collection("users").document(uid).get().addOnSuccessListener { doc ->
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