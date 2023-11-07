package com.maruf.firebaseauth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maruf.firebaseauth.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {

        binding = FragmentProfileBinding.inflate(layoutInflater,null,false)
        return binding.root

    }
/* private fun setProfileInfo(userInfo: ResponseProfileInfo?) {
        if (userInfo != null) {
            binding.name.text = userInfo.name
            binding.phoneNumber.text = userInfo.phone
            binding.email.text = userInfo.email
            binding.address.text = userInfo.address
            binding.nid.text = userInfo.nid
            binding.gender.text = userInfo.gender
            binding.birthdate.text = userInfo.birthdate
            binding.department.text = userInfo.department
            binding.hall.text = userInfo.hall
            binding.bloodGroup.text = userInfo.bloodgroup
            binding.bloodGroup.setTextColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.text_red
                )
            )
            binding.occupation.text = userInfo.occupation
            binding.district.text = userInfo.district

            if (userInfo.subscription == "none") {
                binding.status.text = "Inactive"
                binding.status.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(), R.color.text_red
                    )
                )
            } else {
                binding.status.text = "Active"
                binding.status.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(), R.color.green100
                    )
                )
            }

            if (userInfo.imagePath == null) {
                binding.userProfilePic.hide()
                binding.profilePicAB.show()
                binding.profilePicAB.text = userInfo.name?.let { it1 ->
                    nameAbbreviationGenerator(
                        it1
                    )
                }
            } else {
                binding.userProfilePic.show()
                binding.profilePicAB.hide()

                val profileImg = Constants.IMG_PREFIX + userInfo.imagePath
                Log.i("TAG", "profileImg: $profileImg ")
                binding.userProfilePic.loadImagesWithGlide(profileImg)
            }
        }
    }*/
}





