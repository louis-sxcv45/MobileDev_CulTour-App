package com.example.cultourapp.model.pref

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUserSession(user: UserModel, token: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("userEmail", user.email)
            putString("userDisplayName", user.displayName)
            putString("token", token)
            putString("refreshToken", refreshToken)
            apply()
        }
    }

    fun getSession(): UserModel {
        val email = sharedPreferences.getString("userEmail", "") ?: ""
        val displayName = sharedPreferences.getString("userDisplayName", "") ?: ""
        val token = sharedPreferences.getString("token", "") ?: ""
        val refreshToken = sharedPreferences.getString("refreshToken", "") ?: ""
        return UserModel(email = email, displayName = displayName, token = token, refreshToken = refreshToken)
    }

    fun getUser(): UserModel {
        val email = sharedPreferences.getString("userEmail", "") ?: ""
        return UserModel(email = email)
    }

    fun getToken(): String? = sharedPreferences.getString("token", null)
    fun getRefreshToken(): String? = sharedPreferences.getString("refreshToken", null)

    fun clearSession() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
