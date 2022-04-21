package com.danscoding.mamanstory.response

import com.danscoding.mamanstory.model.SnapStoryModel
import com.google.gson.annotations.SerializedName

data class SnapStoryResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listStory")
    val listStory: ArrayList<SnapStoryModel>,
    @SerializedName("message")
    val message: String
)