package com.danscoding.mamanstory.api

import com.danscoding.mamanstory.model.ModelLogin
import com.danscoding.mamanstory.model.ModelRegister
import com.danscoding.mamanstory.response.ResponseLoginStory
import com.danscoding.mamanstory.response.ResponseRegisterStory
import com.danscoding.mamanstory.response.ResponseStoryPost
import com.danscoding.mamanstory.response.StoryUploadResponse
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

    //another services
    @FormUrlEncoded
    @POST("register")
    suspend fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseRegisterStory

    @FormUrlEncoded
    @POST("login")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseLoginStory

    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?
    ): ResponseStoryPost

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): StoryUploadResponse


}