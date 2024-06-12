package com.example.trashify.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.trashify.data.reponse.UserRepo
import com.example.trashify.data.api.ApiConfig
import com.example.trashify.data.preference.UserModel
import com.example.trashify.data.reponse.ErrorResponse
import com.example.trashify.data.reponse.ListStoryItem
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(private val repository: UserRepo) : ViewModel() {

    private val _getAllStoriesItem = MutableStateFlow<List<ListStoryItem>>(emptyList())
    val getAllStoriesItem = _getAllStoriesItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    init {
        getSession()
    }
    fun getAllStories(token: String){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val apiService = ApiConfig.getApiService()
                val successResponse = apiService.getStories("Bearer $token")
                _getAllStoriesItem.value = successResponse.listStory

                Log.d(TAG, "MainViewModel success: ${successResponse.message}")
            } catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)

                Log.d(TAG, "MainViewModel error: ${errorResponse.message}")
            }
            _isLoading.value = false
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    companion object{
        private const val TAG = "MainViewModel"
    }

}