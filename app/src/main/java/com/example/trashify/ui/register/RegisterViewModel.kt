package com.example.trashify.ui.register

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trashify.data.api.ApiConfig
import com.example.trashify.help.reduceFileImage
import com.example.trashify.help.uriToFile
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val _responseMessage = MutableStateFlow<String?>(null)
    val responseMessage = _responseMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    suspend fun register(name: String, email: String, password: String, confirmPassword: String, imageUri: Uri?) {
        _isLoading.value = true
        try {
            val apiService = ApiConfig.getApiService()

            // Convert Uri to File
            val context = getApplication<Application>().applicationContext
            val file = imageUri?.let { uriToFile(it, context).reduceFileImage() }

            val imageBody = file?.let {
                val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("userImageProfile", it.name, requestFile)
            }
            val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
            val passwordBody = password.toRequestBody("text/plain".toMediaTypeOrNull())
            val confirmPasswordBody = confirmPassword.toRequestBody("text/plain".toMediaTypeOrNull())

            val successResponse = apiService.register(nameBody, emailBody, passwordBody, confirmPasswordBody, imageBody)
            Log.d(TAG, "RegisterViewModel successResponse: ${successResponse.message}")

            _responseMessage.value = successResponse.message
            _registrationSuccess.value = true
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e(TAG, "RegisterViewModel HttpException: $errorBody")
            _responseMessage.value = "Registration failed: ${e.message()}"
            _registrationSuccess.value = false
        } catch (e: Exception) {
            Log.e(TAG, "RegisterViewModel Exception: ${e.message}")
            _responseMessage.value = "Registration failed: ${e.message}"
            _registrationSuccess.value = false
        }
        _isLoading.value = false
    }

    fun resetRegistrationSuccess() {
        _registrationSuccess.value = false
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}
