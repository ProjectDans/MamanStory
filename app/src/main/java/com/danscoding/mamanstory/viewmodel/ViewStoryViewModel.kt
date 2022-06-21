package com.danscoding.mamanstory.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.danscoding.mamanstory.ListStoryPost
import com.danscoding.mamanstory.api.ApiConfiguration
import com.danscoding.mamanstory.preference.PreferenceStoryAccount
import com.danscoding.mamanstory.response.LoginStoryResult
import com.danscoding.mamanstory.response.ResponseStoryPost
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewStoryViewModel (private val prefStory: PreferenceStoryAccount): ViewModel() {

    val listStory = MutableLiveData<ArrayList<ListStoryPost>?>()

    fun setAccountStory(tokenStoryAuth : String){
        Log.d(this@ViewStoryViewModel::class.java.simpleName, tokenStoryAuth)
        ApiConfiguration().getApiService().getViewStory(token = "Bearer $tokenStoryAuth")
            .enqueue(object : Callback<ResponseStoryPost> {
                override fun onResponse(
                    call: Call<ResponseStoryPost>,
                    response: Response<ResponseStoryPost>
                ) {
                    if (response.isSuccessful){
                        listStory.postValue(response.body()?.listStory)
                    } else {
                        listStory.postValue(null)
                    }
                }

                override fun onFailure(call: Call<ResponseStoryPost>, t: Throwable) {
                    listStory.postValue(null)
                }

            })
    }

    fun getStoryAccount(): MutableLiveData<ArrayList<ListStoryPost>?>{
        return listStory
    }

    fun getAccountData(): LiveData<LoginStoryResult> {
        return prefStory.getAccountStory().asLiveData()
    }

    fun storyLogout() {
        viewModelScope.launch {
            prefStory.storyLogoutApp()
        }
    }
}