package com.danscoding.mamanstory

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler


@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    //variable declaration
    private val SPLASH_TIME_OUT: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        //code for splashscreen activity
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}