package com.example.trashify.ui.addstory

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.trashify.R
import com.example.trashify.ViewModelFactory
import com.example.trashify.databinding.ActivityAddStoryBinding
import com.example.trashify.help.getImageUri
import com.example.trashify.help.reduceFileImage
import com.example.trashify.help.uriToFile
import com.example.trashify.ui.main.MainActivity
import com.example.trashify.ui.profile.AboutActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private val viewModel: AddStoryViewModel by viewModels { ViewModelFactory.getInstance(this) }

    private var currentImageUri: Uri? = null
    private var uid: String = ""
    private var caller: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        caller = intent.getStringExtra("caller")

        setupAction()
        observeSession()
        observePredictionResponse()
    }

    private fun observeSession() {
        viewModel.getSession().observe(this) { user ->
            uid = user.uid
            Log.d("AddStoryActivity", "Retrieved UID: $uid")
        }
    }

    private fun observePredictionResponse() {
        viewModel.predictionResponse.observe(this) { prediction ->
            showToast("Prediction: ${prediction.label}")
            navigateToMainActivity()
        }
    }

    private fun setupAction() {
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener {
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this@AddStoryActivity).reduceFileImage()
                val imagePart = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    imageFile.asRequestBody("image/jpeg".toMediaType())
                )
                lifecycleScope.launch {
                    viewModel.uploadImage(uid, imagePart)
                }
            } ?: showToast(getString(R.string.empty_image_warning))
        }

        binding.backButton.setOnClickListener {
            navigateBack()
        }
    }

    private fun navigateBack() {
        when (caller) {
            "MainActivity" -> {
                val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
            "AboutActivity" -> {
                val intent = Intent(this@AddStoryActivity, AboutActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
            else -> finish()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                currentImageUri = uri
                showImage()
            }
        } else {
            Log.d("Photo Picker", "No image selected")
        }
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        launcherGallery.launch(intent)
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        currentImageUri?.let {
            launcherIntentCamera.launch(it)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess: Boolean ->
        if (isSuccess) {
            showImage()
        } else {
            showToast("Failed to take picture")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
