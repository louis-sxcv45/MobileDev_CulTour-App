package com.example.cultourapp.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cultourapp.model.repository.WeatherRepository
import com.example.cultourapp.model.response.ChatbotResponse
import com.example.cultourapp.model.response.Response

class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {
    private val _chatbot = MutableLiveData<ChatbotResponse>()
    val chatbotResponse: LiveData<ChatbotResponse> = _chatbot

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getChatbot(responseObject: Response) {  // Accept Response object directly
        _isLoading.value = true

        weatherRepository.getChatbot(responseObject) { response ->
            _isLoading.value = false
            _chatbot.value = response
        }
    }
}