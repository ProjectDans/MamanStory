package com.danscoding.mamanstory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.danscoding.mamanstory.data.AppRepository
import com.danscoding.mamanstory.data.SnapStoryResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(private val repository:AppRepository): ViewModel() {

    fun delelteAuthToken() {
        viewModelScope.launch {
            repository.setAuthToken("")
        }
    }

    fun getStory(token: String): LiveData<PagingData<SnapStoryResponseItem>>{
        return repository.getStory(token).cachedIn(viewModelScope).asLiveData()
    }
}