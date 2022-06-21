package com.danscoding.mamanstory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.danscoding.mamanstory.databinding.ActivityDetailSnapStoryBinding

class DetailSnapStoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailSnapStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSnapStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        detailStory()
    }

    private fun detailStory() {
        val detailStory = intent.getParcelableExtra<ListStoryPost>("storyData") as ListStoryPost
        binding.tvDetailName.text = detailStory.name
        binding.tvDetailDescription.text = detailStory.description
        Glide.with(this)
            .load(detailStory.photoUrl)
            .centerCrop()
            .into(binding.imageDetail)
    }
}