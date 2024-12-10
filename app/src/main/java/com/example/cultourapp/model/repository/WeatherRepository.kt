package com.example.cultourapp.model.repository

import com.example.cultourapp.model.WeatherApiService
import com.example.cultourapp.model.response.ChatbotResponse
import com.example.cultourapp.model.response.Response
import com.google.gson.Gson
import okhttp3.Callback
import retrofit2.Call

class WeatherRepository(
    private val weatherApiService: WeatherApiService
) {
    fun getChatbot(
        data: Response,
        callback: (ChatbotResponse) -> Unit
    ) {
        weatherApiService.getChatbot(data).enqueue(object : retrofit2.Callback<ChatbotResponse> {
            override fun onResponse(call: Call<ChatbotResponse>, response: retrofit2.Response<ChatbotResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback(responseBody)
                    } else {
//                        callback(ChatbotResponse(success = false, data = null, message = "Empty response"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (!errorBody.isNullOrEmpty()) {
                        try {
                            val errorResponse = Gson().fromJson(errorBody, ChatbotResponse::class.java)
//                            errorResponse.message
                        } catch (e: Exception) {
                            "Failed to parse error message"
                        }
                    } else {
                        "Unknown error occurred"
                    }

//                    callback(ChatbotResponse(success = false, data = null, message = errorMessage))
                }
            }

            override fun onFailure(call: Call<ChatbotResponse>, t: Throwable) {
//                callback(ChatbotResponse(success = false, data = null, message = t.message ?: "Unknown error"))
            }
        })
    }
}