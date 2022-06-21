package com.danscoding.mamanstory.response

import com.google.gson.annotations.SerializedName

data class ResponseRegisterStory(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
)