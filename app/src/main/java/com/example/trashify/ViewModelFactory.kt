package com.example.trashify

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trashify.data.api.ApiConfig
import com.example.trashify.data.api.ApiService
import com.example.trashify.data.database.ProfilePictureRepository
import com.example.trashify.data.reponse.UserRepo
import com.example.trashify.suntik.Injection
import com.example.trashify.ui.addstory.AddStoryViewModel
import com.example.trashify.ui.login.LoginViewModel
import com.example.trashify.ui.main.MainViewModel
import com.example.trashify.ui.profile.AboutViewModel
import com.example.trashify.ui.register.RegisterViewModel

class ViewModelFactory(
    private val application: Application,
    private val repository: UserRepo,
    private val profilePictureRepository: ProfilePictureRepository,
    private val apiService: ApiService
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MainViewModel::class.java -> MainViewModel(repository, apiService)
            LoginViewModel::class.java -> LoginViewModel()
            RegisterViewModel::class.java -> RegisterViewModel(application)
            AddStoryViewModel::class.java -> AddStoryViewModel(repository, apiService)
            AboutViewModel::class.java -> AboutViewModel(repository, profilePictureRepository)
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val apiService = ApiConfig.getApiService()
                val repository = Injection.provideRepository(context)
                val profilePicRepository = Injection.provideProfilePictureRepository()
                INSTANCE ?: ViewModelFactory(
                    context.applicationContext as Application,
                    repository,
                    profilePicRepository,
                    apiService
                ).also { INSTANCE = it }
            }
        }
    }
}