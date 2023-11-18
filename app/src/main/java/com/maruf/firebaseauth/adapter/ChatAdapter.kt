package com.maruf.firebaseauth.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maruf.firebaseauth.R
import com.maruf.firebaseauth.data.chat.Chat


class ChatAdapter(val chatList: List<Chat>, var myID: String) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private val RIGHT = 1
    private val LEFT = 2
    inner  class ChatViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val messageTv = view.findViewById<TextView>(R.id.messagetxt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {

        return if (viewType == RIGHT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.right_chat_ui, parent, false)
            ChatViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.left_chat_ui, parent, false)
            ChatViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.messageTv.text = chat.message
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].senderID == myID) {
            RIGHT
        } else {
            LEFT
        }
    }



}