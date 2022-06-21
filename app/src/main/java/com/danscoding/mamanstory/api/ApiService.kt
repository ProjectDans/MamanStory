package com.danscoding.mamanstory.api

import com.danscoding.mamanstory.model.ModelLogin
import com.danscoding.mamanstory.model.ModelRegister
import com.danscoding.mamanstory.response.ResponseLoginStory
import com.danscoding.mamanstory.response.ResponseRegisterStory
import com.danscoding.mamanstory.response.ResponseStoryPost
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    fun storyRegister(
        @Body register : ModelRegister
    ): Call<ResponseRegisterStory>

    @POST("login")
    fun storyLogin(
        @Body login : ModelLogin
    ): Call<ResponseLoginStory>

    @GET("stories")
    fun getViewStory(@Header("Authorization") token : String ):Call<ResponseStoryPost>

    @Multipart
    @POST("/v1/stories")
    fun uploadSnapStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseRegisterStory>

}