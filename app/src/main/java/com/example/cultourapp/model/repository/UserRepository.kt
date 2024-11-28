package com.example.cultourapp.model.repository

import android.util.Log
import com.example.cultourapp.model.ApiService
import com.example.cultourapp.model.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val apiService: ApiService) {

    fun registerUser(userData: Map<String, String>, callback: (RegisterResponse) -> Unit) {
        apiService.registerUser(userData).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Log.d("RegisterResponse", "Response: ${response.body()}")
                    callback(response.body() ?: RegisterResponse(success = false, message = "Empty response"))
                } else {
                    Log.d("AuthViewModel", "AuthViewModel: ${response.body()}")
                    Log.d("RegisterResponse", "Response: ${response.message()}")
                    callback(RegisterResponse(success = false, message = "Registration failed: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                callback(RegisterResponse(success = false, message = t.message ?: "Network error"))
            }
        })
    }
}
