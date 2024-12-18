package com.example.cultourapp.model.repository

import android.util.Log
import com.example.cultourapp.model.WeatherApiService
import com.example.cultourapp.model.response.ChatbotResponse
import com.example.cultourapp.model.response.errorResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository(
    private val weatherApiService: WeatherApiService
) {
    fun getChatbot(
        data: Map<String, String>,
        callback: (Result<ChatbotResponse>) -> Unit
    ) {
        weatherApiService.getChatbot(data).enqueue(object : Callback<ChatbotResponse> {
            override fun onResponse(call: Call<ChatbotResponse>, response: Response<ChatbotResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback(Result.success(responseBody))
                    } else {
                        callback(Result.failure(Throwable("Empty response")))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        try {
                            val errorResponse = Gson().fromJson(it, errorResponse::class.java)
                            errorResponse.error ?: "Unknown error"
                        } catch (e: Exception) {
                            Log.e("WeatherRepository", "Failed to parse error message", e)
                            "Failed to parse error message"
                        }
                    } ?: "Unknown error occurred"
                    callback(Result.failure(Throwable(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ChatbotResponse>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}
