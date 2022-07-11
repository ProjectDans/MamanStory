package com.danscoding.mamanstory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.danscoding.mamanstory.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: AppRepository): ViewModel(){
    suspend fun userLogin(email: String, password: String) = repository.userLogin(email, password)

    fun saveAuthToken(token: String) {
        viewModelScope.launch {
            repository.setAuthToken(token)
        }
    }
}