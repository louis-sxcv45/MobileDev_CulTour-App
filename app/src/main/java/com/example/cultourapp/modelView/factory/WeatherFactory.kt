package com.example.cultourapp.modelView.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cultourapp.model.di.Injection
import com.example.cultourapp.model.repository.WeatherRepository
import com.example.cultourapp.modelView.WeatherViewModel

class WeatherFactory(
    private val weatherRepository: WeatherRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        WeatherViewModel::class.java -> WeatherViewModel(weatherRepository) as T
        else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: WeatherFactory? = null

        @JvmStatic
        fun getInstance(context: Context): WeatherFactory =
            instance ?: WeatherFactory(
                Injection.provideWeatherRepository(context),
            ).also { instance = it }
    }
}
