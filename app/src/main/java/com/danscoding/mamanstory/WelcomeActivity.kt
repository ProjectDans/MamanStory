package com.danscoding.mamanstory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.danscoding.mamanstory.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}