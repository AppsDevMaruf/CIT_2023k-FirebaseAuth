package com.maruf.firebaseauth

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.maruf.firebaseauth.data.user.UserProfile
import com.maruf.firebaseauth.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var user: FirebaseUser
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseDB: FirebaseDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, null, false)
        binding.apply {
            editProfileBtn.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_profileUpdateFragment)
            }
            btnLogout.setOnClickListener {
                // Inside your fragment or activity
                FirebaseAuth.getInstance().signOut()
// After signing out, navigate to the login page or perform any other necessary actions
                findNavController().navigate(R.id.loginFragment)
            }
        }


        user = FirebaseAuth.getInstance().currentUser!!
        user.let {
            firebaseDB = FirebaseDatabase.getInstance()
            dbRef = firebaseDB.reference.child("user").child(it.uid)
            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(UserProfile::class.java)

                    value?.let { user ->
                        Log.d("TAG", "onDataChange: $user")

                        binding.apply {
                            name.text = user.name
                            email.text = user.email
                            mobile.text = user.mobile
                            userProfilePic.load(user.image)
                        }
                        //set value in view
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("TAG", "Failed to read value.", error.toException())
                }

            })
        }
        return binding.root

    }


}