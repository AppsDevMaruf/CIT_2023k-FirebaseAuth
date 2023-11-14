package com.maruf.firebaseauth.ui

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import coil.load
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.maruf.firebaseauth.data.user.UserProfile
import com.maruf.firebaseauth.databinding.FragmentProfileUpdateBinding


class ProfileUpdateFragment : Fragment() {
    private lateinit var binding: FragmentProfileUpdateBinding
    private lateinit var user: FirebaseUser
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseDB: FirebaseDatabase
    private lateinit var fileUri: Uri
    private lateinit var storageRef: StorageReference
    private var title = "Male"
    lateinit var dialog: ProgressBar
    var userid = 0
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileUpdateBinding.inflate(layoutInflater, null, false)
        user = FirebaseAuth.getInstance().currentUser!!
        firebaseDB = FirebaseDatabase.getInstance()
        binding.updateBtn.setOnClickListener {
            updateProfile()
        }
        binding.uploadProfilePicBtn.setOnClickListener {
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

    private fun updateProfile() {
        val name = binding.name.text.toString()
        val email = binding.phone.text.toString()
        val about = binding.address.text.toString()

        val request = UserProfile(
            name = name,
            email = email,
            about = about,
            image = fileUri.toString(),
            updatedAt = System.currentTimeMillis(),
            userId = user.uid
        )
        updateUser(request, fileUri)
    }

    private fun updateUser(request: UserProfile, uri: Uri) {
       // val userMap = request.asMap()
        if (uri == null) {
            //firebaseDB.reference.child("user").child(request.userId).updateChildren(userMap)
        } else {


            val storageRef = storageRef.child("profile_picture_${System.currentTimeMillis()}.jpg")

            storageRef.putFile(uri).addOnSuccessListener {
                storageRef.downloadUrl.addOnCompleteListener { taskResult ->

                    request.image = taskResult.result.toString()

                    //firebaseDB.reference.child("user").child(request.userId).updateChildren(userMap)

                }.addOnFailureListener {
                    OnFailureListener { e ->
                        Log.d("TAG", "Error : ${e.localizedMessage}")
                    }
                }
            }


        }


    }
}