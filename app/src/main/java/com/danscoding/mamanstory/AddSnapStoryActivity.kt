package com.danscoding.mamanstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.danscoding.mamanstory.databinding.ActivityAddSnapStoryBinding

class AddSnapStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSnapStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSnapStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}