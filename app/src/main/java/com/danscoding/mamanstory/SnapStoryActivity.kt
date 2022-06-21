package com.danscoding.mamanstory

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.danscoding.mamanstory.adapter.AdapterStory
import com.danscoding.mamanstory.databinding.ActivitySnapStoryBinding
import com.danscoding.mamanstory.preference.PreferenceStoryAccount
import com.danscoding.mamanstory.viewmodel.StoryViewModelFactory
import com.danscoding.mamanstory.viewmodel.ViewStoryViewModel

class SnapStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySnapStoryBinding
    private val Context.dataStoreStoryApp: DataStore<Preferences> by preferencesDataStore(name= "storyAccount")
    private lateinit var storyViewModel: ViewStoryViewModel
    private lateinit var adapterStory: AdapterStory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnapStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getListStory()
        binding.btnAddSnapStory.setOnClickListener {
            val intentAddStory = Intent(this@SnapStoryActivity, AddSnapStoryActivity::class.java)
            startActivity(intentAddStory)
        }
        binding.logoutButton.setOnClickListener {
            storyViewModel.storyLogout()
            finish()
        }
        binding.rvSnapStory.layoutManager = LinearLayoutManager(this)
        adapterStory = AdapterStory()
        binding.rvSnapStory.adapter = adapterStory
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun getListStory() {
        val prefStory = PreferenceStoryAccount.getStoryApp(dataStoreStoryApp)
        storyViewModel = ViewModelProvider(
            this, StoryViewModelFactory(prefStory)
        )[ViewStoryViewModel::class.java]

        storyViewModel.getAccountData().observe(this){ storyAccount ->
            if (!storyAccount.isLogin){
                val intentStory = Intent(this@SnapStoryActivity, LoginActivity::class.java)
                intentStory.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intentStory)
            }
            showLoading(true)
            storyViewModel.setAccountStory(tokenStoryAuth = storyAccount.token)
        }

        storyViewModel.getStoryAccount().observe(this){
            if (it!=null){
                adapterStory.setStoryList(it)
                adapterStory.notifyDataSetChanged()
                showLoading(false)
            } else {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}