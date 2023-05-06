package com.ikhsan.storydicoding.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ikhsan.storydicoding.data.local.entity.StoryEntity
import com.ikhsan.storydicoding.data.repository.AuthRepository
import com.ikhsan.storydicoding.data.repository.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel(storyRepository: StoryRepository, private val authRepository: AuthRepository): ViewModel() {

    private val refresh = MutableLiveData<Unit>()
    init {
        refreshData()
    }

    val story: LiveData<PagingData<StoryEntity>> = storyRepository.getAllStory().cachedIn(viewModelScope)

    fun refreshData() {
        refresh.value = Unit
    }

    fun logout() = viewModelScope.launch {
        authRepository.logout()
    }
}