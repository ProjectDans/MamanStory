package com.danscoding.mamanstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.danscoding.mamanstory.databinding.ActivityOtenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}