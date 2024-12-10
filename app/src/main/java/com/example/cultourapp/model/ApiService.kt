package com.example.cultourapp.model

import com.example.cultourapp.model.response.LoginResponse
import com.example.cultourapp.model.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("signup")
    fun registerUser(
        @Body data: Map<String, String>
    ): Call<RegisterResponse>

    @POST("login")
    fun loginUser(
        @Body data: Map<String, String>
    ): Call<LoginResponse>
}