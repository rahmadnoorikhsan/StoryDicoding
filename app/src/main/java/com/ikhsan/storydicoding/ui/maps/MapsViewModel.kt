package com.ikhsan.storydicoding.ui.maps

import androidx.lifecycle.ViewModel
import com.ikhsan.storydicoding.data.repository.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStoryWithLocation() = storyRepository.getStoryWithLocation()
}