package com.ikhsan.storydicoding.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.ikhsan.storydicoding.data.local.datastore.UserPreference
import com.ikhsan.storydicoding.data.remote.RemoteDataSource
import com.ikhsan.storydicoding.data.domain.Result
import com.ikhsan.storydicoding.data.local.database.StoryDatabase
import com.ikhsan.storydicoding.data.local.entity.StoryEntity
import com.ikhsan.storydicoding.data.paging.StoryRemoteMediator
import com.ikhsan.storydicoding.data.remote.response.StoryItem
import com.ikhsan.storydicoding.data.remote.response.UploadStoryResponse
import com.ikhsan.storydicoding.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import java.io.File

class StoryRepository(
    private val remoteDataSource: RemoteDataSource,
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
    ) {
    fun getAllStory(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, userPreference),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getStoryWithLocation(): LiveData<Result<List<StoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = userPreference.getToken().first()
            val response = remoteDataSource.getStory("Bearer $token", location = 1)
            val result = response.listStory
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetailStory(id: String): LiveData<Result<StoryItem>> = liveData {
        emit(Result.Loading)
        try {
            val token = userPreference.getToken().first()
            val response = remoteDataSource.detailStory("Bearer $token", id)
            val result = response.story
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadStory(image: File, description: String): LiveData<Result<UploadStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = userPreference.getToken().first()
            val response = remoteDataSource.uploadStory("Bearer $token", image, description)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}