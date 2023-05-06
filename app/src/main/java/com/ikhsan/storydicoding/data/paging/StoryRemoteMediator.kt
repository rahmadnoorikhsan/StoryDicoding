package com.ikhsan.storydicoding.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ikhsan.storydicoding.data.local.database.StoryDatabase
import com.ikhsan.storydicoding.data.local.datastore.UserPreference
import com.ikhsan.storydicoding.data.local.entity.StoryEntity
import com.ikhsan.storydicoding.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import java.lang.Exception

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val userPreference: UserPreference
) : RemoteMediator<Int, StoryEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX

        try {
            val token = userPreference.getToken().first()
            val responseData = apiService.getAllStory(token, page, state.config.pageSize)
            val endOfPaginationReached = responseData.listStory.isEmpty()
            val storyEntity = responseData.listStory.map {
                StoryEntity(it.id, it.name, it.photoUrl, it.createdAt, it.description, it.lat, it.lon)
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.storyDao().deleteAll()
                }
                database.storyDao().insertStory(storyEntity)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exc: Exception) {
            return MediatorResult.Error(exc)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}