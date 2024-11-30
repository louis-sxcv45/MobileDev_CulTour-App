package com.example.cultourapp.model.di

import android.content.Context
import com.example.cultourapp.model.ApiConfig
import com.example.cultourapp.model.repository.UserRepository
import com.example.cultourapp.model.pref.UserPreferences

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val userPreferences = UserPreferences(context)
        return UserRepository(apiService, userPreferences)
    }
}
