package com.maruf.firebaseauth.data.user


data class UserProfile(
    var userId: String="",
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    var image: String = "",
    var name: String = "",
    var email: String = "",
    var address: String = "",
    var token: String = "",
    var mobile: String? = null
)
