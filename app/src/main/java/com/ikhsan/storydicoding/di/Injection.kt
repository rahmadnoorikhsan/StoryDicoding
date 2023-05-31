package com.ikhsan.storydicoding.di

import android.content.Context
import com.ikhsan.storydicoding.data.local.database.StoryDatabase
import com.ikhsan.storydicoding.data.local.datastore.UserPreference
import com.ikhsan.storydicoding.data.remote.RemoteDataSource
import com.ikhsan.storydicoding.data.remote.retrofit.ApiConfig
import com.ikhsan.storydicoding.data.repository.AuthRepository
import com.ikhsan.storydicoding.data.repository.StoryRepository

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        val remoteDataSource = RemoteDataSource(apiService)
        val userPreference = UserPreference.getInstance(context)
        return AuthRepository(remoteDataSource, userPreference)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val remoteDataSource = RemoteDataSource(apiService)
        val userPreference = UserPreference.getInstance(context)
        val storyDatabase = StoryDatabase.getDatabase(context)
        return StoryRepository(remoteDataSource, userPreference, apiService, storyDatabase)
    }
}