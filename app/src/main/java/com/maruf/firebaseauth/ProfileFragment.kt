package com.maruf.firebaseauth

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.maruf.firebaseauth.data.user.UserProfile
import com.maruf.firebaseauth.data.userInfo.UserInfo
import com.maruf.firebaseauth.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var fileUri: Uri
    private lateinit var user: FirebaseUser
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseDB: FirebaseDatabase
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {

                    fileUri = data?.data!!

                    binding.userProfilePic.load(fileUri)

                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, null, false)
        binding.btnLogout.setOnClickListener {
            // Inside your fragment or activity
            FirebaseAuth.getInstance().signOut()
// After signing out, navigate to the login page or perform any other necessary actions
            findNavController().navigate(R.id.loginFragment)
        }

        user = FirebaseAuth.getInstance().currentUser!!
        user.let {
            firebaseDB = FirebaseDatabase.getInstance()
            dbRef = firebaseDB.reference.child("user").child(it.uid)
            dbRef.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(UserProfile::class.java)

                    value?.let {user->
                        Log.d("TAG", "onDataChange: $user")
                        binding.email.text = user.email
                        binding.userProfilePic.load(user.image)
                        //set value in view
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("TAG", "Failed to read value.", error.toException())
                }

            })
        }
/**/
        binding.uploadProfilePic.setOnClickListener {

            ImagePicker.with(requireActivity())
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }


        }


        return binding.root

    }

    private fun setData(it: UserInfo?) {


    }


}