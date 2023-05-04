package com.ikhsan.storydicoding.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.ikhsan.storydicoding.data.repository.AuthRepository
import com.ikhsan.storydicoding.data.repository.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel(private val storyRepository: StoryRepository, private val authRepository: AuthRepository): ViewModel() {

    private val refresh = MutableLiveData<Unit>()
    init {
        refreshData()
    }

    fun getAllStory() = refresh.switchMap {
        storyRepository.getAllStory()
    }

    fun refreshData() {
        refresh.value = Unit
    }

    fun logout() = viewModelScope.launch {
        authRepository.logout()
    }
}