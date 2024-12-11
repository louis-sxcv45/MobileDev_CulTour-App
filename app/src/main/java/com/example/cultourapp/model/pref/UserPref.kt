package com.example.cultourapp.model.pref

import android.content.Context
import android.content.SharedPreferences
import com.example.cultourapp.model.response.User

class UserPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUserSession(user: User, token: String) {
        sharedPreferences.edit().apply {
            putString("userEmail", user.email)
            putString("userDisplayName", user.displayName)
            putString("token", token)
            apply()
        }
    }

    fun getSession(): UserModel {
        val email = sharedPreferences.getString("userEmail", "") ?: ""
        val token = sharedPreferences.getString("token", "") ?: ""
        return UserModel(email = email,token = token)
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
