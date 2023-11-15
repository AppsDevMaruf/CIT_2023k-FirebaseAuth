package com.maruf.firebaseauth.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.maruf.firebaseauth.data.user.UserProfile
import com.maruf.firebaseauth.databinding.ItemUserBinding


class UserAdapter(private var listener: Listener) : ListAdapter<UserProfile, UserAdapter.UserViewHolder>(Comparator) {
    interface Listener {
        fun profileClicked(userId: String)
        fun messageMeClicked(userId: String)
    }
    inner class UserViewHolder(val itemUserBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(itemUserBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position).let {
            holder.itemUserBinding.apply {
                userName.text = it.name
                userEmail.text = it.email
                userBio.text = it.about
                profileImage.load(it.image)
            }
            holder.itemUserBinding.profileBtn.setOnClickListener { _ ->
                listener.profileClicked(it.userId)
            }
            holder.itemUserBinding.messageBtn.setOnClickListener { _ ->
                listener.messageMeClicked(it.userId)

            }
            holder.itemView.setOnClickListener {

            }



        }


    }
    companion object {

        val Comparator = object : DiffUtil.ItemCallback<UserProfile>() {
            override fun areItemsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
                return oldItem.userId == newItem.userId

            }
        }


    }

}