package com.danscoding.mamanstory

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.danscoding.mamanstory.databinding.ActivitySnapStoryBinding
import com.danscoding.mamanstory.preference.AccountTokenPreference
import com.danscoding.mamanstory.viewmodel.TokenViewModel
import com.danscoding.mamanstory.viewmodel.ViewModelFactory

class SnapStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySnapStoryBinding
    private lateinit var tokenViewModel: TokenViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userToken")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnapStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = AccountTokenPreference.getInstance(dataStore)
        tokenViewModel = ViewModelProvider(this, ViewModelFactory(pref))[TokenViewModel::class.java]

        //aksi untuk intent camera
        binding.btnAddSnapStory.setOnClickListener{addSnapStory()}
        //aksi untuk log out
        binding.logoutButton.setOnClickListener{
            tokenViewModel.removeTokens()
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //INTENT CAMERA
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK){
            val imageBitmap = it.data?.extras?.get("data") as Bitmap
            binding.btnAddSnapStory.setImageBitmap(imageBitmap)
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        launcherIntentCamera.launch(intent)
    }

    private fun addSnapStory() {
        val intent = Intent(this, AddSnapStoryActivity::class.java)
        startActivity(intent)
    }
}