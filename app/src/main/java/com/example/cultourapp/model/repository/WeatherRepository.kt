package com.example.cultourapp.model.repository

import com.example.cultourapp.model.WeatherApiService
import com.example.cultourapp.model.response.ChatbotResponse
import com.example.cultourapp.model.response.Response
import okhttp3.Callback

class WeatherRepository(
    private val weatherApiService: WeatherApiService


) {
        fun getchatbot(
            body:Response
        )
        {
            weatherApiService.getChatbot(body).enqueue(object : Callback<ChatbotResponse> {
                
            })

        }
}