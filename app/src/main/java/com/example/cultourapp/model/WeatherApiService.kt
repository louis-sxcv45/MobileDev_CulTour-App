package com.example.cultourapp.model

import com.example.cultourapp.model.response.ChatbotResponse
import com.example.cultourapp.model.response.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WeatherApiService {
    @POST("chatbot")
    fun getChatbot(
        @Body data: Response
    ): Call<ChatbotResponse>


}