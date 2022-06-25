@file:Suppress("PrivatePropertyName")

package com.danscoding.mamanstory

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    //variable declaration
    private val SPLASH_TIME_OUT: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        //code for splashscreen activity
        Handler().postDelayed({
            startActivity(Intent(this, SnapStoryActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}