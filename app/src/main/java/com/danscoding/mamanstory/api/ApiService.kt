package com.danscoding.mamanstory.api

import com.danscoding.mamanstory.response.AccountLoginResponse
import com.danscoding.mamanstory.response.AccountRegisterResponse
import com.danscoding.mamanstory.response.SnapStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun registerAccountTask(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String): Call<AccountRegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginAccountTask(
        @Field("email") email: String,
        @Field("password") password: String): Call<AccountLoginResponse>

    @GET("stories")
    fun getSnapStoryTask(
        @Header("Authorization") token: String
    ): Call<SnapStoryResponse>

    @Multipart
    @POST("stories")
    fun uploadSnapStoryTask(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<AccountRegisterResponse>

}