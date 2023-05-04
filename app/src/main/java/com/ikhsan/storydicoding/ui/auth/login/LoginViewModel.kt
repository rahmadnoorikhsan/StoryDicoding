package com.ikhsan.storydicoding.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ikhsan.storydicoding.data.domain.Result
import com.ikhsan.storydicoding.data.remote.response.LoginResponse
import com.ikhsan.storydicoding.data.repository.AuthRepository

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        return authRepository.login(email, password)
    }
}