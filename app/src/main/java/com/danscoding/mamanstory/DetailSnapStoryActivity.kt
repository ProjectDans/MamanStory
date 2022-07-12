package com.danscoding.mamanstory

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.danscoding.mamanstory.data.SnapStoryResponseItem
import com.danscoding.mamanstory.databinding.ActivityDetailSnapStoryBinding

class DetailSnapStoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailSnapStoryBinding
    private lateinit var story: SnapStoryResponseItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSnapStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        story = intent.getParcelableExtra(EXTRA_DETAIL)!!

        binding.apply {
            loadImage(story.photoUrl)
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
        }
    }

    private fun loadImage(url: String?) {
        Glide.with(this@DetailSnapStoryActivity)
            .load(url)
            .into(binding.imageDetail)
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}