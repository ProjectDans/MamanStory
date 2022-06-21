package com.danscoding.mamanstory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.danscoding.mamanstory.preference.PreferenceStoryAccount

class StoryViewModelFactory (private val preferenceStoryAccount: PreferenceStoryAccount): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_LAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ViewStoryViewModel::class.java) -> {
                ViewStoryViewModel(preferenceStoryAccount) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(preferenceStoryAccount) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}