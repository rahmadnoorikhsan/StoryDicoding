package com.ikhsan.storydicoding.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ikhsan.storydicoding.di.Injection
import com.ikhsan.storydicoding.ui.auth.login.LoginViewModel
import com.ikhsan.storydicoding.ui.auth.register.RegisterViewModel
import com.ikhsan.storydicoding.ui.detail.DetailViewModel
import com.ikhsan.storydicoding.ui.main.MainViewModel
import com.ikhsan.storydicoding.ui.splash.RoutingViewModel
import com.ikhsan.storydicoding.ui.add.upload.UploadViewModel
import com.ikhsan.storydicoding.ui.maps.MapsViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(Injection.provideAuthRepository(context)) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(Injection.provideAuthRepository(context)) as T
            }
            modelClass.isAssignableFrom(RoutingViewModel::class.java) -> {
                RoutingViewModel(Injection.provideAuthRepository(context)) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(Injection.provideStoryRepository(context), Injection.provideAuthRepository(context)) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(Injection.provideStoryRepository(context)) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(Injection.provideStoryRepository(context)) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(Injection.provideStoryRepository(context)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}