package com.example.trashify.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.trashify.R
import com.example.trashify.ViewModelFactory
import com.example.trashify.data.api.ApiConfig
import com.example.trashify.data.preference.UserModel
import com.example.trashify.data.preference.UserPreference
import com.example.trashify.data.preference.dataStore
import com.example.trashify.data.reponse.UserRepo
import com.example.trashify.databinding.ActivityLoginBinding
import com.example.trashify.ui.main.MainActivity
import com.example.trashify.ui.register.RegisterActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var userRepo: UserRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataStore = this.dataStore
        val userPreference = UserPreference.getInstance(dataStore)
        val apiService = ApiConfig.getApiService()
        userRepo = UserRepo.getInstance(userPreference, apiService)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.RegisterTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            email = findViewById(R.id.ed_login_email)
            password = findViewById(R.id.ed_login_password)

            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            lifecycleScope.launch {
                val loginResult = viewModel.login(emailText, passwordText)
                if (loginResult != null) {
                    Log.d("LoginActivity", "Login Result: $loginResult")

                    val user = UserModel(
                        email = loginResult.email ?: "",
                        uid = loginResult.uid ?: "",
                        name = loginResult.name ?: "",
                        userImageProfile = loginResult.userImageProfile ?: "",
                        isLogin = true
                    )

                    Log.d("LoginActivity", "User Model: $user")

                    saveSession(user)

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    val message = viewModel.responseMsg.value ?: "Login failed"
                    showToast(message)
                }
            }
        }
    }

    private suspend fun saveSession(user: UserModel) {
        try {
            Log.d(TAG, "Saving session for user: $user")
            userRepo.saveSession(user)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save session", e)
            showToast("Failed to save session")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupView() {
        window.insetsController?.hide(WindowInsets.Type.statusBars())
        supportActionBar?.hide()

        lifecycleScope.launch {
            viewModel.isLoading.collect { showLoading(it) }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleText = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val messageText = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailText = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val passwordText = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)

        val emailEditText = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordEditText = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)

        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val signUp = ObjectAnimator.ofFloat(binding.RegisterTextView, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(signUp, login)
        }

        AnimatorSet().apply {
            playSequentially(
                titleText,
                messageText,
                emailText,
                emailEditText,
                passwordText,
                passwordEditText,
                together
            )
            start()
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
