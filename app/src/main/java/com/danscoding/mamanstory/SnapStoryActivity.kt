package com.danscoding.mamanstory

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danscoding.mamanstory.adapter.AdapterStory
import com.danscoding.mamanstory.databinding.ActivitySnapStoryBinding
import com.danscoding.mamanstory.preference.PreferenceStoryAccount
import com.danscoding.mamanstory.viewmodel.HomeViewModel
import com.danscoding.mamanstory.viewmodel.StoryViewModelFactory
import com.danscoding.mamanstory.viewmodel.ViewStoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalPagingApi::class)
@AndroidEntryPoint
class SnapStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySnapStoryBinding
    private lateinit var recyclerView: RecyclerView
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapterStory: AdapterStory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnapStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Maman Story"

        adapterStory = AdapterStory()
        recyclerView = binding.rvSnapStory
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflate = menuInflater
        inflate.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.btnSetting -> {
                Intent(this, SettingActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.btnMapsActivity -> {
                Intent(this, MapsActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


//    @SuppressLint("NotifyDataSetChanged")
//    private fun getListStory() {
//        val prefStory = PreferenceStoryAccount.getStoryApp(dataStoreStoryApp)
//        storyViewModel = ViewModelProvider(
//            this, StoryViewModelFactory(prefStory)
//        )[ViewStoryViewModel::class.java]
//
//        storyViewModel.getAccountData().observe(this){ storyAccount ->
//            if (!storyAccount.isLogin){
//                val intentStory = Intent(this@SnapStoryActivity, LoginActivity::class.java)
//                intentStory.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intentStory)
//            }
//            showLoading(true)
//            storyViewModel.setAccountStory(tokenStoryAuth = storyAccount.token)
//        }
//
//        storyViewModel.getStoryAccount().observe(this){
//            if (it!=null){
//                adapterStory.setStoryList(it)
//                adapterStory.notifyDataSetChanged()
//                showLoading(false)
//            } else {
//                showLoading(false)
//            }
//        }
//    }

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