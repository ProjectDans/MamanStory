package com.danscoding.mamanstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.danscoding.mamanstory.SnapStoryActivity.Companion.EXTRA_TOKEN
import com.danscoding.mamanstory.databinding.ActivityAddSnapStoryBinding
import com.danscoding.mamanstory.utils.createCustomTempFile
import com.danscoding.mamanstory.utils.reduceFileImage
import com.danscoding.mamanstory.utils.rotateBitmap
import com.danscoding.mamanstory.utils.uriToFile
import com.danscoding.mamanstory.viewmodel.UploadViewModel
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
import java.io.FileOutputStream

@OptIn(ExperimentalPagingApi::class)
@AndroidEntryPoint
class AddSnapStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSnapStoryBinding
    private lateinit var token: String
    private lateinit var currentPhotoPath: String
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
                 startCamera()
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
                    .collect{ response: Result<Any> ->
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
                            if (desc.text.toString().isEmpty()){
                                Toast.makeText(
                                    this@AddSnapStoryActivity, "Please Fill Your Description Story", Toast.LENGTH_SHORT
                                ).show()
                                showLoading(false)
                            } else {
                                Toast.makeText(this@AddSnapStoryActivity, "Upload Failed", Toast.LENGTH_SHORT).show()
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

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.danscoding.mamanstory",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result: Bitmap = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                true
            )
            val fos = FileOutputStream(myFile)
            result.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            getFile = myFile
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
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

}


