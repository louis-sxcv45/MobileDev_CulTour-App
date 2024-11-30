package com.example.cultourapp.model.pref

data class UserModel(
    val email: String = "Unkown Email",
    val displayName: String = "Unknown Display Name",
    val token: String? = null,
    val refreshToken: String? = null
)