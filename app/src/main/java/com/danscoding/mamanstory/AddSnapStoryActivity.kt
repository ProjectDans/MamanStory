package com.danscoding.mamanstory

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.danscoding.mamanstory.api.ApiConfiguration
import com.danscoding.mamanstory.databinding.ActivityAddSnapStoryBinding
import com.danscoding.mamanstory.preference.PreferenceStoryAccount
import com.danscoding.mamanstory.response.ResponseRegisterStory
import com.danscoding.mamanstory.utils.reduceFileImage
import com.danscoding.mamanstory.utils.rotateBitmap
import com.danscoding.mamanstory.utils.uriToFile
import com.danscoding.mamanstory.viewmodel.StoryViewModelFactory
import com.danscoding.mamanstory.viewmodel.ViewStoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddSnapStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSnapStoryBinding
    private val Context.dataStoreStoryApp: DataStore<Preferences> by preferencesDataStore(name= "storyAccount")
    private lateinit var addSnapStory: ViewStoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSnapStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!havePermission()){
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSION,
                REQUEST_CODE_PERMISSION
            )
        }
        setupActionButton()
        postStoryViewModel()
    }

    private fun setupActionButton(){
        binding.btnOpenCamera.setOnClickListener { startCameraX() }
        binding.btnOpenGallery.setOnClickListener { addImage() }
        binding.btnUploadStory.setOnClickListener { lifecycleScope.launch(Dispatchers.Main) { postStory() } }
    }

    private fun startCameraX() {
        val intentCameraX = Intent(this, CameraActivity::class.java)
        launcherCamera.launch(intentCameraX)
    }

    private fun postStoryViewModel() {
        val preference = PreferenceStoryAccount.getStoryApp(dataStoreStoryApp)
        addSnapStory = ViewModelProvider(this, StoryViewModelFactory(preference))[ViewStoryViewModel::class.java]
    }

    private fun addImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val choosePicture = Intent.createChooser(intent, "Pick a Picture")
        launchGallery.launch(choosePicture)
    }

    private fun postStory() {
        if (getFile != null){
            val fileImage = reduceFileImage(getFile as File)
            val description = binding.edtTextStory.text.toString().toRequestBody("text/plain".toMediaType())
            val request = fileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val multiparImage : MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                fileImage.name,
                request
            )
            binding.btnUploadStory.visibility = View.GONE
            showLoading(true)
            addSnapStory.getAccountData().observe(this){
                user ->
                val start = ApiConfiguration().getApiService().uploadSnapStory(multiparImage, description, "Bearer ${user.token}")
                start.enqueue(object : Callback<ResponseRegisterStory>{
                    override fun onResponse(
                        call: Call<ResponseRegisterStory>,
                        response: Response<ResponseRegisterStory>
                    ) {
                        if (response.isSuccessful){
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error){
                                Toast.makeText(this@AddSnapStoryActivity, "Upload Story Successfully", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        } else {
                            Toast.makeText(this@AddSnapStoryActivity, response.message(), Toast.LENGTH_SHORT).show()
                            binding.btnUploadStory.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<ResponseRegisterStory>, t: Throwable) {
                        showLoading(false)
                        Toast.makeText(this@AddSnapStoryActivity, "Bad Response", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        } else {
            Toast.makeText(this@AddSnapStoryActivity, "Insert your picture", Toast.LENGTH_SHORT).show()
        }
    }

    private var getFile: File? = null
    private val launchGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode == RESULT_OK){
            val selectedPicture : Uri = result.data?.data as Uri
            val filePicture = uriToFile(selectedPicture, this@AddSnapStoryActivity)
            getFile = filePicture

            binding.previewImageView.setImageURI(selectedPicture)
        }
    }

    private val launcherCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == CAMERA_X_RESULT_CODE){
            val fileCamera = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = fileCamera
            val result = rotateBitmap(BitmapFactory.decodeFile(getFile?.path), isBackCamera)
            binding.previewImageView.setImageBitmap(result)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_PERMISSION){
            if(!havePermission()){
                Toast.makeText(this, "No Permission Granted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun havePermission() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object{
        const val CAMERA_X_RESULT_CODE = 200
        private val REQUIRED_PERMISSION = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }

}


