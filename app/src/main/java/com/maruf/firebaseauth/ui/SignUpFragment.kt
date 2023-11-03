package com.maruf.firebaseauth.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.maruf.firebaseauth.R
import com.maruf.firebaseauth.databinding.FragmentSignUpBinding
import com.maruf.firebaseauth.model.userInfo.UserInfo
import com.maruf.firebaseauth.utils.FirebaseUtils.firebaseAuth
import com.maruf.firebaseauth.utils.FirebaseUtils.firebaseUser
import com.maruf.firebaseauth.viewmodel.FirebaseViewModel


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions
    private val firebaseVM by activityViewModels<FirebaseViewModel>()

    // Define an ActivityResultLauncher for Google Sign-In
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater, null, false)

        // Initialize the Google Sign-In launcher
        googleSignInLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                onGoogleSignInActivityResult(result.resultCode, result.data)
            }
        //create Request
        createRequest()
        binding.btnSignUP.setOnClickListener {
            signUpUser()
        }

        binding.tvRedirectLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        binding.googleSignIn.setOnClickListener {
            signInWithGoogle()
        }


        return binding.root
    }

    private fun onGoogleSignInActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //sign in with google
    private fun signUpUser() {
        val email = binding.etSEmailAddress.text.toString()
        val pass = binding.etSPassword.text.toString()
        val confirmPassword = binding.etSConfPassword.text.toString()

        // check pass
        if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(
                requireActivity(), "Email and Password can't be blank", Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (pass != confirmPassword) {
            Toast.makeText(
                requireActivity(), "Password and Confirm Password do not match", Toast.LENGTH_SHORT
            ).show()
            return
        }
        // If all credential are correct
        // We call createUserWithEmailAndPassword
        // using auth object and pass the
        // email and pass in it.
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                Toast.makeText(requireActivity(), "Successfully Singed Up", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireActivity(), "Singed Up Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createRequest() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser

                    if (user != null) {
                        val displayName = user.displayName ?: "No Name"
                        val email = user.email ?: "No Email"
                        val photoUrl = user.photoUrl?.toString() ?: ""
                        val phone=user.phoneNumber
                        // Create a UserInfo object
                        val userInfo = UserInfo(displayName, email, photoUrl)
                        firebaseVM.saveUserData(userInfo)

                        findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                    }
                } else {
                    Toast.makeText(requireContext(), "Google Sign-In Failed", Toast.LENGTH_SHORT)
                        .show()
                }

            }
    }


}
