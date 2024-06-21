package com.example.trashify.data.reponse

import android.content.ContentValues.TAG
import android.util.Log
import com.example.trashify.data.preference.UserModel
import com.example.trashify.data.preference.UserPreference
import com.example.trashify.data.api.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepo private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
        Log.d(TAG, "Session saved in UserRepo")
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout(uid: String) {
        try {
            val logoutResponse = apiService.logout(LogoutRequest(uid))
            if (!logoutResponse.error) {
                userPreference.logout()
                Log.d(TAG, "Logged out successfully: ${logoutResponse.message}")
            } else {
                Log.d(TAG, "Logout failed: ${logoutResponse.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logout error: ${e.localizedMessage}", e)
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepo? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepo =
            instance ?: synchronized(this) {
                instance ?: UserRepo(userPreference, apiService).also { instance = it }
            }
    }
}
