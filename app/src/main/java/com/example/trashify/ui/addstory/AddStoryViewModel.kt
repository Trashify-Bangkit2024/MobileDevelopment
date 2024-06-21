package com.example.trashify.ui.addstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.trashify.data.api.ApiService
import com.example.trashify.data.preference.UserModel
import com.example.trashify.data.reponse.ErrorResponse
import com.example.trashify.data.reponse.PredictionResponse
import com.example.trashify.data.reponse.UserRepo
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class AddStoryViewModel(
    private val repository: UserRepo,
    private val apiService: ApiService
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _response = MutableStateFlow("")

    private val _predictionResponse = MutableLiveData<PredictionResponse>()
    val predictionResponse: LiveData<PredictionResponse> = _predictionResponse

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    suspend fun uploadImage(uid: String, imagePart: MultipartBody.Part) {
        _isLoading.value = true
        try {
            val uidBody = uid.toRequestBody("text/plain".toMediaType())
            val predictionResponse = apiService.postPrediction(uidBody, imagePart)
            _response.value = "Image uploaded successfully"
            _predictionResponse.postValue(predictionResponse)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            _response.value = errorResponse.message
        } catch (e: Exception) {
            Log.e(TAG, "Error during image upload: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }

    companion object {
        private const val TAG = "AddStoryViewModel"
    }
}