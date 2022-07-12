package com.danscoding.mamanstory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.danscoding.mamanstory.SnapStoryActivity.Companion.EXTRA_TOKEN
import com.danscoding.mamanstory.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewModelScope.launch {
            viewModel.getAuthToken().collect { token ->
                if (token.isNullOrEmpty()) {
                    Intent(this@MainActivity, OtenActivity::class.java).also { intent ->
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Intent(this@MainActivity, SnapStoryActivity::class.java).also { intent ->
                        intent.putExtra(EXTRA_TOKEN, token)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}