package com.danscoding.mamanstory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.danscoding.mamanstory.data.AppRepository
import com.danscoding.mamanstory.response.ResponseStoryPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: AppRepository): ViewModel() {

    fun getStoriesLocation(token: String): Flow<Result<ResponseStoryPost>> =
        repository.getStoriesLocation(token)

    fun getAuthToken(): Flow<String?> = repository.getAuthToken()
}