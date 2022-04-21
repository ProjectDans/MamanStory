package com.danscoding.mamanstory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.danscoding.mamanstory.preference.AccountTokenPreference
import kotlinx.coroutines.launch

class TokenViewModel(private val pref: AccountTokenPreference) : ViewModel() {
    fun getTokens(): LiveData<String?> {
        return pref.getToken().asLiveData()
    }

    fun saveTokens(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun removeTokens() {
        viewModelScope.launch {
            pref.removeToken()
        }
    }
}