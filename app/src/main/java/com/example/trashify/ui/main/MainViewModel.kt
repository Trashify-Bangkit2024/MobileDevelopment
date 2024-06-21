package com.example.trashify.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.trashify.data.api.ApiService
import com.example.trashify.data.preference.UserModel
import com.example.trashify.data.reponse.PredictionResponse
import com.example.trashify.data.reponse.UserRepo
import kotlinx.coroutines.launch

class MainViewModel(
    repository: UserRepo,
    private val apiService: ApiService
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _historyPredictionResponse = MutableLiveData<List<PredictionResponse>>()
    val historyPredictionResponse: LiveData<List<PredictionResponse>> = _historyPredictionResponse

    val session: LiveData<UserModel> = repository.getSession().asLiveData()

    fun getPrediction(uid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "Attempting to retrieve prediction for UID: $uid")
            try {
                val historyPrediction = apiService.getHistoryPrediction(uid)
                Log.d(TAG, "Prediction retrieved: $historyPrediction")
                _historyPredictionResponse.postValue(historyPrediction.predictions)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get prediction for UID: $uid, Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
