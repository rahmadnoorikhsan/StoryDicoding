package com.ikhsan.storydicoding.ui.add.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ikhsan.storydicoding.data.repository.StoryRepository
import com.ikhsan.storydicoding.data.domain.Result
import com.ikhsan.storydicoding.data.remote.response.UploadStoryResponse
import java.io.File

class UploadViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun storyUpload(image: File, description: String): LiveData<Result<UploadStoryResponse>> {
        return storyRepository.uploadStory(image, description)
    }
}