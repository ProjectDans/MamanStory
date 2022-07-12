package com.danscoding.mamanstory

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danscoding.mamanstory.adapter.LoadingStateAdapter
import com.danscoding.mamanstory.adapter.StoryAdapter
import com.danscoding.mamanstory.databinding.ActivitySnapStoryBinding
import com.danscoding.mamanstory.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalPagingApi::class)
@AndroidEntryPoint
class SnapStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySnapStoryBinding
    private lateinit var recyclerView: RecyclerView
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapterStory: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnapStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Maman Story"

        adapterStory = StoryAdapter()
        recyclerView = binding.rvSnapStory
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterStory.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapterStory.retry()
            }
        )
        recyclerView.setHasFixedSize(true)

        viewModel.getStory(intent.getStringExtra(EXTRA_TOKEN)!!).observe(this){
            adapterStory.submitData(lifecycle, it)
            showLoading(false)
        }

        binding.btnAddSnapStory.setOnClickListener {
            Intent(this, AddSnapStoryActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflate = menuInflater
        inflate.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.btnMapsActivity -> {
                Intent(this, MapsActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.btnLogout -> {
                viewModel.delelteAuthToken()
                Intent(this, OtenActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}