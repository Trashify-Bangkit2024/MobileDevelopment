package com.example.trashify.suntik

import android.content.Context
import com.example.trashify.data.reponse.UserRepo
import com.example.trashify.data.preference.UserPreference
import com.example.trashify.data.preference.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepo {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepo.getInstance(pref)
    }
}