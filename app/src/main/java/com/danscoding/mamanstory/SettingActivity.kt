package com.danscoding.mamanstory

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.danscoding.mamanstory.databinding.ActivitySettingBinding
import com.danscoding.mamanstory.preference.PreferenceStoryAccount
import com.danscoding.mamanstory.viewmodel.StoryViewModelFactory
import com.danscoding.mamanstory.viewmodel.ViewStoryViewModel

class SettingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingBinding
    private val Context.dataStoreStoryApp: DataStore<Preferences> by preferencesDataStore(name= "storyAccount")
    private lateinit var storyViewModel: ViewStoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Settings"
        storyModel()
        //button logout
        binding.btnLogout.setOnClickListener {
            storyViewModel.storyLogout()
            finish()
        }
    }

    private fun storyModel() {
        val prefStory = PreferenceStoryAccount.getStoryApp(dataStoreStoryApp)
        storyViewModel = ViewModelProvider(
            this, StoryViewModelFactory(prefStory)
        )[ViewStoryViewModel::class.java]
        storyViewModel.getAccountData().observe(this) { account ->
            if (!account.isLogin){
                val intent = Intent(this@SettingActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            storyViewModel.setAccountStory(tokenStoryAuth = account.token)
        }
    }
}