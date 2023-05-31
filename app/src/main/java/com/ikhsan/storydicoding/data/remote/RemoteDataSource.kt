package com.ikhsan.storydicoding.data.remote

import com.ikhsan.storydicoding.data.remote.response.*
import com.ikhsan.storydicoding.data.remote.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun getStory(token: String, page: Int? = null, size: Int? = null, location: Int = 0): StoryResponse {
        return apiService.getAllStory(token, page, size, location)
    }

    suspend fun detailStory(token: String, id: String) = apiService.detailStory(token, id)

    suspend fun uploadStory(token: String, image: File, description: String): UploadStoryResponse {
        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        val requestImage = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            image.name,
            requestImage
        )

        return apiService.uploadStory(token, imageMultipart, requestDescription)
    }
}