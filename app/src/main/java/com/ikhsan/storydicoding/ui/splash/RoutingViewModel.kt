package com.ikhsan.storydicoding.ui.splash

import androidx.lifecycle.ViewModel
import com.ikhsan.storydicoding.data.repository.AuthRepository

class RoutingViewModel(private val authRepository: AuthRepository): ViewModel() {
    fun isLogin() = authRepository.isLogin()
}