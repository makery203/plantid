package com.example.plantid

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.activity.viewModels
import com.example.plantid.databinding.ActivityMainBinding
import com.example.plantid.model.PlantViewModel
import com.example.plantid.model.PlantViewModelFactory
import com.example.plantid.network.ApiClient
import com.example.plantid.ui.PlantDetailActivity
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService

    private val plantViewModel: PlantViewModel by viewModels {
        PlantViewModelFactory(ApiClient.apiService)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let { uri ->
                    identifyWithViewModel(uri)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        startCamera()

        binding.btnCapture.setOnClickListener { takePhoto() }
        binding.btnGallery.setOnClickListener { openGallery() }
        binding.btnLogout.setOnClickListener {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
        }

        observeViewModel()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = binding.cameraPreview.surfaceProvider
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.e("MainActivity", "Camera binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val photoFile = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val uri = Uri.fromFile(photoFile)
                    identifyWithViewModel(uri)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("MainActivity", "Capture failed", exception)
                }
            }
        )
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun identifyWithViewModel(uri: Uri) {
        lifecycleScope.launch {
            val imageFile = uriToFile(uri)
            plantViewModel.identifyPlant(imageFile)
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("plant_image", ".jpg", cacheDir)
        inputStream?.use { input -> tempFile.outputStream().use { input.copyTo(it) } }
        return tempFile
    }

    private fun observeViewModel() {
        plantViewModel.plantDetails.observe(this) { plant ->
            if (plant != null) {
                val intent = Intent(this, PlantDetailActivity::class.java)
                intent.putExtra("plant", plant)
                startActivity(intent)
            }
        }

        plantViewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        plantViewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
