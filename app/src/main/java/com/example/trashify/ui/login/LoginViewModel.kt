package com.example.trashify.ui.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.trashify.data.api.ApiConfig
import com.example.trashify.data.reponse.LoginResponse
import com.example.trashify.data.reponse.LoginResult
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException

class LoginViewModel : ViewModel() {
    val isLoading = MutableStateFlow(false)
    val responseMsg = MutableStateFlow<String?>(null)

    suspend fun login(email: String, password: String): LoginResult? {
        isLoading.value = true
        return try {
            val apiService = ApiConfig.getApiService()
            val body = mapOf(
                "email" to email,
                "password" to password
            )
            val response = apiService.login(body)
            Log.d(TAG, "Full response: ${response.uid}")
            isLoading.value = false
            responseMsg.value = response.message

            response.uid?.let { userDetail ->
                Log.d(TAG, "User detail extracted: $userDetail")
                LoginResult(
                    name = userDetail.userName,
                    uid = userDetail.uid,
                    email = userDetail.email,
                    userImageProfile = userDetail.userImageProfile
                )
            }
        } catch (e: HttpException) {
            isLoading.value = false
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            responseMsg.value = errorResponse.message
            Log.e(TAG, "Login error: ${errorResponse.message}", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "General error", e)
            responseMsg.value = "General error"
            isLoading.value = false
            null
        }
    }
}
