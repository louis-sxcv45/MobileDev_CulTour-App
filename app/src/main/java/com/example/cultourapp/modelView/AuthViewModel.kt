package com.example.cultourapp.modelView

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cultourapp.model.repository.UserRepository
import com.example.cultourapp.model.response.LoginResponse
import com.example.cultourapp.model.response.RegisterResponse
import com.example.cultourapp.model.response.UserData
import com.example.cultourapp.model.pref.UserModel
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse?>()
    val registerResponse: LiveData<RegisterResponse?> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> = _loginResponse

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun registerUser(userData: Map<String, String>) {
        _loading.postValue(true)
        userRepository.registerUser(userData) { response ->
            _loading.postValue(false)
            if (response.success) {
                _registerResponse.postValue(response)
            } else {
                _registerResponse.postValue(
                    RegisterResponse(
                        success = false,
                        data = null,
                        message = response.message
                    )
                )
            }
        }
    }

    fun loginUser(userData: Map<String, String>) {
        _loading.postValue(true)
        userRepository.loginUser(userData) { response ->
            _loading.postValue(false)
            _loginResponse.postValue(response)
            if (response.success) {
                response.data?.let { userData ->
                    storeUserSession(userData)
                }
            }
        }
    }

    private fun storeUserSession(user: UserData) {
        viewModelScope.launch {
            userRepository.saveUserSession(user)
            Log.d("AuthViewModel", "User session stored: $user")
        }
    }

    fun getUserSession(): UserModel {
        return userRepository.getSession()
    }
}
