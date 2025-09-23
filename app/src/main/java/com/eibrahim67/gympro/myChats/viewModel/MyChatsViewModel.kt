package com.eibrahim67.gympro.myChats.viewModel

import androidx.lifecycle.ViewModel
import com.eibrahim67.gympro.myChats.model.Chat
import com.eibrahim67.gympro.myChats.model.UserChat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyChatsViewModel : ViewModel() {

    fun listenUserChats(onChat: (Chat) -> Unit) {
        val uid = FirebaseAuth.getInstance().uid ?: return
        val db = FirebaseFirestore.getInstance()
        val userChatsRef = db.collection("userChats").document(uid)

        userChatsRef.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null || !snapshot.exists()) return@addSnapshotListener

            // get list of chatIds from userChats
            val chatIds = snapshot.get("chats") as? List<String> ?: emptyList()

            for (chatId in chatIds) {
                db.collection("chats").document(chatId)
                    .addSnapshotListener { chatDoc, err ->
                        if (err != null || chatDoc == null || !chatDoc.exists()) return@addSnapshotListener

                        val members = chatDoc.get("members") as? List<String> ?: emptyList()
                        val lastMessage = chatDoc.getString("lastMessage") ?: ""
                        val lastTime = chatDoc.getTimestamp("lastMessageTime")

                        val otherUid = members.firstOrNull { it != uid } ?: uid

                        onChat(
                            Chat(
                                chatId = chatId,
                                otherUid = otherUid,
                                lastMessage = lastMessage,
                                lastMessageTime = lastTime
                            )
                        )
                    }
            }
        }
    }

    fun getUserInfo(uid: String, onUser: (UserChat) -> Unit) {
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