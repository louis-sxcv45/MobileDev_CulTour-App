package com.example.cultourapp.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cultourapp.model.repository.UserRepository
import com.example.cultourapp.model.response.RegisterResponse

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse?>()
    val registerResponse: LiveData<RegisterResponse?> = _registerResponse


    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun registerUser(userData: Map<String, String>) {
        _loading.postValue(true)
        userRepository.registerUser(userData, ::handleResponse)
    }

    private fun handleResponse(response: RegisterResponse) {
        _loading.postValue(false)
        if (response.success == true) {
            _registerResponse.postValue(response)
        } else {
            _registerResponse.postValue(response)
        }
    }
}
