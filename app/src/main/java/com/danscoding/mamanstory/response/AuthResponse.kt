package com.danscoding.mamanstory.response

import com.google.gson.annotations.SerializedName

data class ResponseLoginStory(
    @field:SerializedName("error")
    val error: String,

    @field:SerializedName("message")
    val message: Boolean,

    @field:SerializedName("loginResult")
    val loginResult: LoginResult
)

data class LoginResult(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String
)

data class ResponseRegisterStory(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
