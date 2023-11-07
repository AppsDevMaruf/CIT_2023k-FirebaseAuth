package com.maruf.firebaseauth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import coil.load
import com.maruf.firebaseauth.databinding.FragmentProfileBinding
import com.maruf.firebaseauth.data.userInfo.UserInfo
import com.maruf.firebaseauth.viewmodel.FirebaseViewModel


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val firebaseVM by activityViewModels<FirebaseViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, null, false)



        firebaseVM.user.observe(viewLifecycleOwner) {
            binding.profileName.text = it.displayName
            binding.email.text = it.email
            binding.profilePic.load(it.photoUrl)
            Toast.makeText(requireContext(), "${it.displayName}", Toast.LENGTH_SHORT).show()
        }



        return binding.root

    }

    private fun setData(it: UserInfo?) {


    }


}