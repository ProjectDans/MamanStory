package com.danscoding.mamanstory.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.danscoding.mamanstory.api.ApiService
import com.danscoding.mamanstory.data.database.SnapStoryDatabase
import com.danscoding.mamanstory.response.ResponseLoginStory
import com.danscoding.mamanstory.response.ResponseRegisterStory
import com.danscoding.mamanstory.response.ResponseStoryPost
import com.danscoding.mamanstory.response.StoryUploadResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class AppRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferences: AppPreference,
    private val storyDatabase: SnapStoryDatabase
) {
    fun getAuthToken(): Flow<String?> = preferences.getUserToken()

    suspend fun setAuthToken(token: String){
        preferences.setUserToken(token)
    }

    suspend fun userRegister(
        name: String,
        email: String,
        password: String
    ): Flow<Result<ResponseRegisterStory>> = flow {
        try {
            val registerResponse = apiService.userRegister(name, email, password)
            Log.d("200", "Register Success")
            emit(Result.success(registerResponse))
        } catch (e: Exception) {
            Log.e("400", "Register Failed")
            emit(Result.failure(e))
        }
    }

    suspend fun userLogin(email: String, password: String): Flow<Result<ResponseLoginStory>> = flow {
        try {
            val response = apiService.userLogin(email, password)
            Log.d("200", "Login Success")
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("400", "Login Failed")
            emit(Result.failure(e))
        }
    }

    fun getStory(token: String): Flow<PagingData<SnapStoryResponseItem>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = SnapStoryRemoteMediator(storyDatabase, apiService, "Bearer$token"),
            pagingSourceFactory ={
                storyDatabase.storyDao().getAllStory()
            }
        ).flow
    }

    fun getStoriesLocation(token: String): Flow<Result<ResponseStoryPost>> = flow{
        try {
            val userToken = "Bearer $token"
            val response = apiService.getStory(userToken, null, size = 100, location = 1)
            Log.d("200", "Success")
            emit(Result.success(response))
        } catch (e: Exception){
            Log.e("400", "Failed")
            emit(Result.failure(e))
        }
    }

    suspend fun uploadPhoto(
        token: String, file: MultipartBody.Part, description: RequestBody, lat: RequestBody?, lon: RequestBody?
    ): Flow<Result<StoryUploadResponse>> = flow {
        try {
            val userToken = "Bearer $token"
            val response = apiService.uploadImage(userToken, file,description, lat, lon)
            Log.d("200", "Success")
            emit(Result.success(response))
        } catch (e: Exception){
            Log.e("400", "Failed")
            emit(Result.failure(e))
        }
    }
}