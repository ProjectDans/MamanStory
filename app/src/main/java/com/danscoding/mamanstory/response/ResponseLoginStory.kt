package com.danscoding.mamanstory.response

import com.google.gson.annotations.SerializedName

data class ResponseLoginStory(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult: LoginStoryResult
)

data class LoginStoryResult(
    @SerializedName("name")
    val name: String,

    @SerializedName("token")
    val token: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("isLogin")
    val isLogin: Boolean
)
