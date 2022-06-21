package com.danscoding.mamanstory

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.danscoding.mamanstory.databinding.ActivityCameraBinding
import com.danscoding.mamanstory.utils.createFile

class CameraActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var takePhoto : ImageCapture? = null
    private var startCamera : CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openCamera()
    }

    private fun openCamera() {
        binding.captureImage.setOnClickListener {
            getImage()
        }
        binding.switchCamera.setOnClickListener {
            startCamera = if (startCamera == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA

            startCameraStory()
        }
    }

    public override fun onResume() {
        super.onResume()
        startCameraStory()
        hideStorySystem()
    }

    private fun getImage(){
        val imageCaptureStory = takePhoto?:return
        val filePhotoStory = createFile(application)
        val outputOptionsStream = ImageCapture.OutputFileOptions.Builder(filePhotoStory).build()
        imageCaptureStory.takePicture(
            outputOptionsStream, ContextCompat.getMainExecutor(this),
            object: ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val intentCamera = Intent()
                    intentCamera.putExtra("picture", filePhotoStory)
                    intentCamera.putExtra("isBackCamera", startCamera == CameraSelector.DEFAULT_BACK_CAMERA)
                    setResult(AddSnapStoryActivity.CAMERA_X_RESULT_CODE, intentCamera)
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@CameraActivity, "Failed to capture image", Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun startCameraStory() {
        val cameraProvider = ProcessCameraProvider.getInstance(this)
        cameraProvider.addListener({
            val providerCamera : ProcessCameraProvider = cameraProvider.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewCamera.surfaceProvider)
                }
            takePhoto = ImageCapture.Builder().build()
            try {
                providerCamera.unbindAll()
                providerCamera.bindToLifecycle(
                    this,
                    startCamera,
                    preview,
                    takePhoto
                )
            } catch (e: Exception){
                Toast.makeText( this@CameraActivity, "Failed to open camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun hideStorySystem() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

}