package com.maruf.firebaseauth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.maruf.firebaseauth.adapter.UserAdapter
import com.maruf.firebaseauth.data.user.UserProfile
import com.maruf.firebaseauth.databinding.FragmentUsersBinding
import com.maruf.firebaseauth.utils.FirebaseUtils


class UsersFragment : Fragment() {
    private lateinit var binding: FragmentUsersBinding
    private lateinit var adapter: UserAdapter
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseDB: FirebaseDatabase

    var userList = mutableListOf<UserProfile>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentUsersBinding.inflate(layoutInflater, null, false)
        adapter = UserAdapter(object :UserAdapter.Listener{
            override fun profileClicked(userId: String) {

            }

            override fun messageMeClicked(userId: String) {
                findNavController().navigate(
                    R.id.action_homeFragment_to_chatFragment,
                    bundleOf(FirebaseUtils.REMOTE_USER_KEY to userId)
                )
            }
        })
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        currentUserID?.let {currentUser->
            firebaseDB = FirebaseDatabase.getInstance()
            dbRef = firebaseDB.reference.child("user")
            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()

                    snapshot.children.forEach { dataSnapshot ->
                        val value = dataSnapshot.getValue(UserProfile::class.java)
                        value?.let { user ->
                            if (user.userId!=currentUser){
                                userList.add(user)
                            }
                        }
                    }
                    // Submit the list and set the adapter outside the loop
                    adapter.submitList(userList)
                    binding.userRcv.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("TAG", "Failed to read value.", error.toException())
                }

            })
        }
        return binding.root

    }


}