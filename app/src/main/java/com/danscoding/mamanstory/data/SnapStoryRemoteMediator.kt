package com.danscoding.mamanstory.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.danscoding.mamanstory.api.ApiService
import com.danscoding.mamanstory.data.database.RemoteKey
import com.danscoding.mamanstory.data.database.SnapStoryDatabase

@OptIn(ExperimentalPagingApi::class)
class SnapStoryRemoteMediator(
    private val database: SnapStoryDatabase,
    private val apiService: ApiService,
    private val token: String
): RemoteMediator<Int, SnapStoryResponseItem>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SnapStoryResponseItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKey?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                val nextKey = remoteKey?.nextKey?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                nextKey
            }
        }
        try {
            val responseData = apiService.getStory(token, page,state.config.pageSize, null)
            val eofPaginationReached = responseData.listStory.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH){
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (eofPaginationReached) null else page + 1
                val key = responseData.listStory.map {
                    RemoteKey(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                database.remoteKeysDao().insertAll(key)
                responseData.listStory.forEach {
                    val story = SnapStoryResponseItem(
                        it.id,
                        it.name,
                        it.description,
                        it.photoUrl,
                        it.createdAt,
                        it.lat,
                        it.lon
                    )
                    database.storyDao().insertStory(story)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = eofPaginationReached)
        } catch (exception: Exception) {

            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, SnapStoryResponseItem>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, SnapStoryResponseItem>): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, SnapStoryResponseItem>): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}