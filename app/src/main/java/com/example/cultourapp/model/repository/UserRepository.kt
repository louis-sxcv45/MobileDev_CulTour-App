package com.example.cultourapp.model.repository

import android.util.Log
import com.example.cultourapp.model.ApiService
import com.example.cultourapp.model.response.LoginResponse
import com.example.cultourapp.model.response.RegisterResponse
import com.example.cultourapp.model.pref.UserModel
import com.example.cultourapp.model.pref.UserPreferences
import com.example.cultourapp.model.response.UserData
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {

    fun registerUser(
        userData: Map<String, String>,
        callback: (RegisterResponse) -> Unit
    ) {
        apiService.registerUser(userData).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback(responseBody)
                    } else {
                        callback(RegisterResponse(success = false, data = null, message = "Empty response"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (!errorBody.isNullOrEmpty()) {
                        try {
                            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                            errorResponse.message
                        } catch (e: Exception) {
                            "Failed to parse error message"
                        }
                    } else {
                        "Unknown error occurred"
                    }

                    callback(RegisterResponse(success = false, data = null, message = errorMessage))
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                callback(RegisterResponse(success = false, data = null, message = t.message ?: "Unknown error"))
            }
        })
    }

    fun loginUser (
        userData: Map<String, String>,
        callback: (LoginResponse) -> Unit
    ) {
        apiService.loginUser(userData).enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null) {
                        responseBody.data?.let { userData ->
                            saveUserSession(userData)
                            Log.d("data", userData.toString())
                        }
                        callback(responseBody)
                    } else {
                        callback(LoginResponse(success = false, data = null, message = "Empty response"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (!errorBody.isNullOrEmpty()) {
                        try {
                            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                            errorResponse.message
                        } catch (e: Exception) {
                            "Failed to parse error message"
                        }
                    } else {
                        "Unknown error occurred"
                    }

                    callback(LoginResponse(success = false, data = null, message = errorMessage))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(LoginResponse(success = false, data = null, message = t.message ?: "Unknown error"))
            }
        })
    }

    fun saveUserSession(user: UserData) {
        userPreferences.saveUserSession(user.user, user.token)
    }

    fun getSession(): UserModel {
        return userPreferences.getSession()
    }

    fun getUser(): UserModel {
        return userPreferences.getUser()
    }

    fun clearUserSession() {
        userPreferences.clearSession()
    }
}

