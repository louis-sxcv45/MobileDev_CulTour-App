package com.example.cultourapp.model

import com.example.cultourapp.model.response.ChatbotResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WeatherApiService {
    @POST("chatbot")
    fun getChatbot(
        @Body data: Map<String, String>
    ): Call<ChatbotResponse>
}