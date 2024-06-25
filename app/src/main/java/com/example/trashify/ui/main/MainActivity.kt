package com.example.trashify.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trashify.R
import com.example.trashify.ViewModelFactory
import com.example.trashify.data.reponse.PredictionResponse
import com.example.trashify.databinding.ActivityMainBinding
import com.example.trashify.ui.adaptasi.StoryAdapter
import com.example.trashify.ui.addstory.AddStoryActivity
import com.example.trashify.ui.login.LoginActivity
import com.example.trashify.ui.profile.AboutActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPermissions()
        setupView()
        setupObservers()
        setupNavigation()
    }

    private fun setupPermissions() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private fun setupNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Main -> true
                R.id.About -> {
                    navigateToAbout()
                    true
                }
                else -> false
            }
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            intent.putExtra("caller", "MainActivity")
            startActivity(intent)
        }
    }

    private fun navigateToAbout() {
        viewModel.session.value?.let { user ->
            if (user.uid.isNotEmpty()) {
                Log.d("MainActivity", "Navigating to AboutActivity with UID: ${user.uid}")
                val intent = Intent(this, AboutActivity::class.java).apply {
                    putExtra("uid", user.uid)
                    putExtra("name", user.name)
                    putExtra("email", user.email)
                    putExtra("userImageProfile", user.userImageProfile)
                }
                startActivity(intent)
            } else {
                Log.e("MainActivity", "UID is null or empty")
            }
        }
    }

    private fun setupView() {
        window.insetsController?.hide(WindowInsets.Type.statusBars())

        binding.bottomNavigationView.apply {
            background = null
            menu.getItem(1).isEnabled = false
            selectedItemId = R.id.Main
        }

        binding.rvListStories.layoutManager = LinearLayoutManager(this)
        binding.rvListStories.adapter = StoryAdapter()
    }

    private fun setupObservers() {
        viewModel.session.observe(this) { user ->
            if (user != null && user.uid.isNotEmpty()) {
                viewModel.getPrediction(user.uid)
            } else {
                startLoginActivity()
            }
        }

        viewModel.historyPredictionResponse.observe(this) { predictions ->
            setAllStoriesList(predictions)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setAllStoriesList(items: List<PredictionResponse>?) {
        if (items.isNullOrEmpty()) {
            binding.textViewNoStories.visibility = View.VISIBLE
            binding.rvListStories.visibility = View.GONE
            binding.backgroundImageView.visibility = View.VISIBLE
            showToast("No stories available")
        } else {
            binding.textViewNoStories.visibility = View.GONE
            binding.rvListStories.visibility = View.VISIBLE
            binding.backgroundImageView.visibility = View.GONE

            binding.rvListStories.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = StoryAdapter().apply { submitList(items) }
                setHasFixedSize(true)
            }
            showToast("Result ${items.size}")
        }
    }

    private fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            showToast("Permission request denied")
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
