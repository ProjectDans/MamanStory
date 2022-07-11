package com.danscoding.mamanstory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.danscoding.mamanstory.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: AppRepository): ViewModel() {

    fun getAuthToken(): Flow<String?> = repository.getAuthToken()
}