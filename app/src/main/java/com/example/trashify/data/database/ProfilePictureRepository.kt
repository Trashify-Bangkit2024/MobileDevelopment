package com.example.trashify.data.database

import android.util.Log
import com.example.trashify.data.api.ApiService
import com.example.trashify.data.reponse.UpdateProfileResponse
import com.example.trashify.data.reponse.UserProfileResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProfilePictureRepository(private val apiService: ApiService) {
    suspend fun updateProfilePicture(userId: String, imagePath: String): UpdateProfileResponse {
        try {
            val imageFile = File(imagePath)
            val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("userImageProfile", imageFile.name, requestFile)

            val uidRequestBody = userId.toRequestBody("text/plain".toMediaTypeOrNull())
            Log.d("ProfilePicRepo", "Updating profile picture for user: $userId")

            val response = apiService.updateUserProfile(uidRequestBody, imagePart)
            Log.d("ProfilePicRepo", "Profile picture update successful: ${response.userImageProfile}")

            return response
        } catch (e: Exception) {
            Log.e("ProfilePicRepo", "Error updating profile picture: ${e.message}")
            throw e
        }
    }

    suspend fun getProfilePicture(userId: String): UserProfileResponse {
        return apiService.getUserProfile(userId)
    }
}
