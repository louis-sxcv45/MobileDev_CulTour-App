package com.example.cultourapp.model.repository

import android.util.Log
import com.example.cultourapp.model.ApiService
import com.example.cultourapp.model.response.LoginResponse
import com.example.cultourapp.model.response.RegisterResponse
import com.example.cultourapp.model.pref.UserModel
import com.example.cultourapp.model.pref.UserPreferences
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
                            val email = userData.user.email ?: "Unknown Email"
                            val displayName = userData.user.displayName ?: "Unknown Display Name"

                            val userModel = UserModel(email = email, displayName = displayName, token = userData.token, refreshToken = userData.refreshToken)

                            saveUserSession(userModel)
                            Log.d("data", userModel.toString())
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

    fun refreshToken(callback: (Boolean) -> Unit) {
        val refreshToken = getRefreshToken()
        if (refreshToken.isNullOrEmpty()) {
            callback(false)
            return
        }

        val requestBody = mapOf("refreshToken" to refreshToken)

        // Call to the refresh token endpoint (not loginUser)
        apiService.loginUser(requestBody).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseData = response.body()?.data
                    if (responseData != null) {
                        // Assuming responseData contains the updated token and user information
                        val userData = responseData.user
                        val email = userData.email ?: "Unknown Email"
                        val displayName = userData.displayName ?: "Unknown Display Name"

                        // Create UserModel object for session
                        val userModel = UserModel(
                            email = email,
                            displayName = displayName,
                            token = responseData.token,
                            refreshToken = responseData.refreshToken
                        )

                        // Save the refreshed user session
                        saveUserSession(userModel)
                        Log.d("data", responseData.toString())

                        callback(true)
                    } else {
                        callback(false)
                    }
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(false)
            }
        })
    }


    fun getToken(): String? {
        return userPreferences.getToken()
    }

    private fun getRefreshToken(): String? {
        return userPreferences.getRefreshToken()
    }

    fun saveUserSession(userModel: UserModel) {
        userPreferences.saveUserSession(userModel, userModel.token ?: "", userModel.refreshToken ?: "")
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

