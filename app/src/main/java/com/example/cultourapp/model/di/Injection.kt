package com.example.cultourapp.model.di

import android.content.Context
import com.example.cultourapp.model.ApiConfig
import com.example.cultourapp.model.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository(apiService)
    }
}
