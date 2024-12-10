package com.example.cultourapp.model.di

import android.content.Context
import com.example.cultourapp.model.ApiConfig
import com.example.cultourapp.model.WeatherApiConfig
import com.example.cultourapp.model.repository.UserRepository
import com.example.cultourapp.model.pref.UserPreferences
import com.example.cultourapp.model.repository.WeatherRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val userPreferences = UserPreferences(context)
        return UserRepository(apiService, userPreferences)
    }
    fun provideWeatherRepository(context: Context): WeatherRepository {
        val apiService = WeatherApiConfig.getApiService()
        return WeatherRepository(apiService)
    }
}
