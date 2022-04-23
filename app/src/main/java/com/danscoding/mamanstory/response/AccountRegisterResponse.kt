package com.danscoding.mamanstory.response

import com.google.gson.annotations.SerializedName

data class AccountRegisterResponse(
    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("message")
    val message: String? = null
)
