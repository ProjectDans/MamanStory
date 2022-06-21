package com.danscoding.mamanstory.response

import com.danscoding.mamanstory.ListStoryPost
import com.google.gson.annotations.SerializedName

data class ResponseStoryPost(

    @field:SerializedName("error")
    val error:Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("listStory")
    val listStory: ArrayList<ListStoryPost>
)