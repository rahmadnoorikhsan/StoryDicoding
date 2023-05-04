package com.ikhsan.storydicoding.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ikhsan.storydicoding.data.remote.response.RegisterResponse
import com.ikhsan.storydicoding.data.domain.Result
import com.ikhsan.storydicoding.data.repository.AuthRepository

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> {
        return authRepository.register(name, email, password)
    }
}