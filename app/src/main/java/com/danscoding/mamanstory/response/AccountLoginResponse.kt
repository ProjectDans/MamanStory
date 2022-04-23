package com.danscoding.mamanstory.response

import com.danscoding.mamanstory.model.LoginResultModel
import com.google.gson.annotations.SerializedName

data class AccountLoginResponse(
    @field:SerializedName("error")
    val error: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult: LoginResultModel
)