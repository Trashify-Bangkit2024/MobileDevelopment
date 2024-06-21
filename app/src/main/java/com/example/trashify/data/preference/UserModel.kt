package com.example.trashify.data.preference

data class UserModel(
    val email: String,
    val uid: String,
    val name: String,
    val userImageProfile: String,
    val isLogin: Boolean
)