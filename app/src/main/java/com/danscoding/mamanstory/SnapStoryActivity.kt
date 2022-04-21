package com.danscoding.mamanstory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.danscoding.mamanstory.databinding.ActivitySnapStoryBinding
import com.danscoding.mamanstory.viewmodel.TokenViewModel

class SnapStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySnapStoryBinding
    private lateinit var tokenViewModel: TokenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnapStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //aksi untuk log out
        binding.logoutButton.setOnClickListener{
            tokenViewModel.removeTokens()
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}