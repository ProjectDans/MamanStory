package com.danscoding.mamanstory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.danscoding.mamanstory.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: AppRepository): ViewModel() {
    suspend fun userRegister(name: String, email: String, password: String) =
        repository.userRegister(name, email, password)
}