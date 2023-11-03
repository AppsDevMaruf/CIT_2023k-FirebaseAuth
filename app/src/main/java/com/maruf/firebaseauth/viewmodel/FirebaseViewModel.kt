package com.maruf.firebaseauth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maruf.firebaseauth.model.userInfo.UserInfo

class FirebaseViewModel : ViewModel() {
    private val _user = MutableLiveData<UserInfo>()
    val user: LiveData<UserInfo> = _user

    // function to send message
    fun saveUserData(user: UserInfo) {
        _user.value = user
    }
}