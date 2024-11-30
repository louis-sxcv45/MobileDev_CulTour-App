package com.example.cultourapp.modelView.factory

import android.content.Context
import com.example.cultourapp.modelView.AuthViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cultourapp.model.di.Injection
import com.example.cultourapp.model.repository.UserRepository

class ViewModelFactory(
    private val userRepository: UserRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        AuthViewModel::class.java -> AuthViewModel(userRepository) as T
        else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: ViewModelFactory(
                Injection.provideUserRepository(context),
            ).also { instance = it }
    }
}
