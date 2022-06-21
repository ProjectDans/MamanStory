package com.danscoding.mamanstory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.danscoding.mamanstory.preference.PreferenceStoryAccount
import com.danscoding.mamanstory.response.LoginStoryResult
import kotlinx.coroutines.launch

class StoryViewModel(private val preferenceStoryAccount: PreferenceStoryAccount): ViewModel() {

    fun getStoryAccount(): LiveData<LoginStoryResult>{
        return preferenceStoryAccount.getAccountStory().asLiveData()
    }

    fun saveStoryAccount(loginStory: LoginStoryResult){
        viewModelScope.launch {
            preferenceStoryAccount.storyAccountSave(LoginStoryResult(loginStory.userId, loginStory.name, loginStory.token, loginStory.isLogin))
        }
    }

}