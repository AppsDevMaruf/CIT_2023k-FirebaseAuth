package com.maruf.firebaseauth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.maruf.firebaseauth.adapter.ChatAdapter
import com.maruf.firebaseauth.data.chat.Chat
import com.maruf.firebaseauth.databinding.FragmentChatBinding
import com.maruf.firebaseauth.utils.FirebaseUtils
import com.maruf.firebaseauth.utils.FirebaseUtils.CHATS
import java.util.UUID

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    lateinit var adapter: ChatAdapter
    private lateinit var user: FirebaseUser
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseDB: FirebaseDatabase
    var chatList = mutableListOf<Chat>()

    private var remoteUserID: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        checkArgument()

        binding = FragmentChatBinding.inflate(layoutInflater, null, false)
        user = FirebaseAuth.getInstance().currentUser!!

        firebaseDB = FirebaseDatabase.getInstance()
        dbRef = firebaseDB.reference.child(CHATS)
        binding.sendBtn.setOnClickListener {
            sendMgs()
        }

        //chat list

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                snapshot.children.forEach { dataSnapshot ->
                    val value = dataSnapshot.getValue(Chat::class.java)
                    value?.let { chat ->
                        if (chat.receiverID == user.uid && chat.senderID == remoteUserID || chat.receiverID == remoteUserID && chat.senderID == user.uid) {
                            chatList.add(chat)
                            val sortedChat = chatList.sortedBy { it.timeStamp }
                            adapter = ChatAdapter(sortedChat, user.uid)
                            binding.chatRCV.adapter = adapter
                            Log.d("TAG", "onDataChange: $chatList")
                            Log.d("TAG", "sortedChatOnDataChange: $sortedChat")
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        return binding.root
    }

    private fun sendMgs() {
        val message = binding.textInputEdt.text.toString().trim()
        val chatID = UUID.randomUUID().toString()

        val chat = Chat(
            senderID = user.uid,
            receiverID = remoteUserID,
            message = message,
            chatID = chatID,
            timeStamp = System.currentTimeMillis()
        )
        dbRef.child(chat.chatID).setValue(chat).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireActivity(), "mgs send successfully", Toast.LENGTH_SHORT)
                    .show()

            }
        }.addOnFailureListener {
            Log.d("TAG", "${it.localizedMessage}: ")
        }
        binding.textInputEdt.text?.clear()
    }

    private fun checkArgument() {
        // Retrieve the value from the arguments bundle
        arguments?.getString(FirebaseUtils.REMOTE_USER_KEY)?.let {
            remoteUserID = it

        }
    }
}