package com.example.trashify.ui.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.trashify.R
import com.example.trashify.ViewModelFactory
import com.example.trashify.databinding.ActivityAboutBinding
import com.example.trashify.help.reduceFileImage
import com.example.trashify.help.uriToFile
import com.example.trashify.ui.addstory.AddStoryActivity
import com.example.trashify.ui.login.LoginActivity
import com.example.trashify.ui.main.MainActivity

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    private val viewModel by viewModels<AboutViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()

        // removing the shadow effect on the BottomNavigationView
        binding.bottomNavigationView.background = null

        // making the placeholder menu item unclickable
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false

        // Set the About item as selected
        binding.bottomNavigationView.selectedItemId = R.id.About

        setupActionBar()
        setupIntentData()
        setupClickListeners()
        observeProfilePicture()
    }

    private fun setupBinding() {
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = "About"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupIntentData() {
        val name = intent.getStringExtra("name") ?: "No Name"
        val email = intent.getStringExtra("email") ?: "No Email"
        uid = intent.getStringExtra("uid") ?: ""

        binding.aboutName.text = name
        binding.aboutGmail.text = email

        if (uid.isNotEmpty()) {
            viewModel.fetchUserProfile(uid)
        } else {
            Log.e("AboutActivity", "UID is not initialized or empty")
        }
    }

    private fun setupClickListeners() {
        binding.logout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Log Out?")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log Out") { _, _ ->
                    viewModel.logout(uid)
                    val loginIntent = Intent(this@AboutActivity, LoginActivity::class.java)
                    startActivity(loginIntent)
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.addProfilePicture.setOnClickListener {
            openGalleryForImage()
        }

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Main -> {
                    val intent = Intent(this@AboutActivity, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.About -> {
                    // this button will have no effect
                    true
                }
                else -> false
            }
        }

        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                val pictureFile = uriToFile(uri, this@AboutActivity)
                val reducedFile = pictureFile.reduceFileImage()
                val picturePath = reducedFile.absolutePath

                Log.d("AboutActivity", "Uploading new profile picture: $picturePath")
                viewModel.updateProfilePicture(uid, picturePath) { newProfileUrl ->
                    Log.d("AboutActivity", "Profile picture callback received: $newProfileUrl")
                    binding.profileImageView.load(newProfileUrl)
                }
            }
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun observeProfilePicture() {
        viewModel.userProfile.observe(this) { userProfile ->
            userProfile?.userImageProfile?.let { newProfileUrl ->
                Log.d("AboutActivity", "Observed profile picture update: $newProfileUrl")
                binding.profileImageView.load(newProfileUrl)
            } ?: Log.d("AboutActivity", "No profile picture URL available")
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        galleryLauncher.launch(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
