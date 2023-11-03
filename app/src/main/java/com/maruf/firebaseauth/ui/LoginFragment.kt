package com.maruf.firebaseauth.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.maruf.firebaseauth.R
import com.maruf.firebaseauth.databinding.FragmentLoginBinding
import com.maruf.firebaseauth.databinding.FragmentSignUpBinding
import com.maruf.firebaseauth.utils.FirebaseUtils.firebaseAuth


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, null, false)

        binding.buttonLogin.setOnClickListener {
            login()
        }
        return binding.root
    }

    private fun login() {
        val email = binding.editTextEmailAddress.text.toString()
        val pass = binding.editTextPassword.text.toString()
        // calling signInWithEmailAndPassword(email, pass)
        // function using Firebase auth object
        // On successful response Display a Toast
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                Toast.makeText(requireActivity(), "Successfully LoggedIn", Toast.LENGTH_SHORT)
                    .show()
            } else
                Toast.makeText(requireActivity(), "Log In failed ", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val user = firebaseAuth.currentUser
        if (user != null) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }


    }
}