package com.danscoding.mamanstory

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.danscoding.mamanstory.SnapStoryActivity.Companion.EXTRA_TOKEN
import com.danscoding.mamanstory.databinding.ActivityAddSnapStoryBinding
import com.danscoding.mamanstory.utils.reduceFileImage
import com.danscoding.mamanstory.utils.rotateBitmap
import com.danscoding.mamanstory.utils.uriToFile
import com.danscoding.mamanstory.viewmodel.UploadViewModel
import com.danscoding.mamanstory.viewmodel.ViewStoryViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@OptIn(ExperimentalPagingApi::class)
@AndroidEntryPoint
class AddSnapStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSnapStoryBinding
    private val Context.dataStoreStoryApp: DataStore<Preferences> by preferencesDataStore(name= "storyAccount")
    private lateinit var addSnapStory: ViewStoryViewModel
    private lateinit var currentPhotoPath: String
    private lateinit var token: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var getFile: File? = null
    private var location: Location? = null
    private var lat: RequestBody? = null
    private var lon: RequestBody? = null
    private val viewModel: UploadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSnapStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        viewModel.viewModelScope.launch {
            viewModel.getAuthToken().collect {
                token = it!!
            }
        }

        getUserLocation()
         binding.apply {
             btnUploadStory.setOnClickListener {
                 uploadStory()
             }
             btnOpenCamera.setOnClickListener {
                 startCameraX()
             }
             btnOpenGallery.setOnClickListener {
                 openGallery()
             }
         }
    }

    private fun uploadStory() {
        if (getFile != null) {
            showLoading(true)
            val desc = binding.edtTextStory
            val file = reduceFileImage(getFile as File)
            val description = binding.edtTextStory.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo", file.name, requestImageFile
            )
            if (location != null){
                lat = location?.latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                lon = location?.longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            }

            viewModel.viewModelScope.launch {
                viewModel.uploadImage(token, imageMultipart, description, lat, lon)
                    .collect{ response ->
                        response.onSuccess {
                            Intent(this@AddSnapStoryActivity, SnapStoryActivity::class.java).also {
                                it.apply {
                                    putExtra(EXTRA_TOKEN, token)
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                }
                                startActivity(it)
                                finish()
                            }
                        }
                        response.onFailure {
                            if (desc.text.toString().isEmpty()) {
                                Toast.makeText(
                                    this@AddSnapStoryActivity,
                                    getString(R.string.story_desc_warning),
                                    Toast.LENGTH_SHORT
                                ).show()
                                showLoading(false)
                            } else {
                                Toast.makeText(
                                    this@AddSnapStoryActivity,
                                    getString(R.string.upload_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                                showLoading(false)
                            }
                        }
                    }
            }
        } else {
            showLoading(false)
            Toast.makeText(
                this,
                getString(R.string.upload_warning),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getUserLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    this.location = it
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.app_permission_warning),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getUserLocation()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.app_permission_warning),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun startCameraX() {
        val intentCameraX = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intentCameraX)
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == CAMERA_X_RESULT_CODE){
            val fileCamera = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = fileCamera
            val result = rotateBitmap(BitmapFactory.decodeFile(getFile?.path), isBackCamera)
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK){
            val selectedPhoto: Uri = result.data?.data as Uri
            val file = uriToFile(selectedPhoto, this@AddSnapStoryActivity)
            getFile = file
            binding.previewImageView.setImageURI(selectedPhoto)
        }
    }

    companion object{
        const val CAMERA_X_RESULT_CODE = 200
        private val REQUIRED_PERMISSION = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }

}


