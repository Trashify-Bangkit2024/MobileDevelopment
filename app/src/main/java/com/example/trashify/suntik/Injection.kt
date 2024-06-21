package com.example.trashify.suntik

import android.content.Context
import com.example.trashify.data.api.ApiConfig
import com.example.trashify.data.database.ProfilePictureRepository
import com.example.trashify.data.preference.UserPreference
import com.example.trashify.data.preference.dataStore
import com.example.trashify.data.reponse.UserRepo

object Injection {
    fun provideRepository(context: Context): UserRepo {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepo.getInstance(pref, apiService)
    }

    fun provideProfilePictureRepository(): ProfilePictureRepository {
        val apiService = ApiConfig.getApiService()
        return ProfilePictureRepository(apiService)
    }
}
