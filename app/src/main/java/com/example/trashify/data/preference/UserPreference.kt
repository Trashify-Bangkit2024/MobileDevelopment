package com.example.trashify.data.preference

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        try {
            Log.d(TAG, "Saving session for user: $user")
            dataStore.edit { preferences ->
                preferences[EMAIL_KEY] = user.email
                preferences[UID_KEY] = user.uid
                preferences[NAME_KEY] = user.name
                preferences[IMAGE_PROFILE_KEY] = user.userImageProfile
                preferences[LOGIN_KEY] = user.isLogin
            }
            Log.d(TAG, "Session saved successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save session", e)
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[EMAIL_KEY] ?: "",
                preferences[UID_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[IMAGE_PROFILE_KEY] ?: "",
                preferences[LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private var INSTANCE: UserPreference? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val UID_KEY = stringPreferencesKey("uid")
        private val NAME_KEY = stringPreferencesKey("name")
        private val IMAGE_PROFILE_KEY = stringPreferencesKey("userImageProfile")
        private val LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: UserPreference(dataStore).also { INSTANCE = it }
        }
    }
}
