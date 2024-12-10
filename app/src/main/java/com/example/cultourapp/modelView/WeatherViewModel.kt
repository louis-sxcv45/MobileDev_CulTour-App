package com.example.cultourapp.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cultourapp.model.repository.WeatherRepository
import com.example.cultourapp.model.response.ChatbotResponse

class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _chatbot = MutableLiveData<ChatbotResponse>()
    val chatbotResponse: LiveData<ChatbotResponse> = _chatbot

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getChatbot(province: String, startDate: String, endDate: String) {
        _isLoading.value = true

        val requestData = mapOf(
            "province" to province,
            "start_date" to startDate,
            "end_date" to endDate
        )

        weatherRepository.getChatbot(requestData) { result ->
            _isLoading.value = false
            result.onSuccess { response ->
                _chatbot.value = response
            }
            result.onFailure { throwable ->
                _errorMessage.value = throwable.message
            }
        }
    }
}
