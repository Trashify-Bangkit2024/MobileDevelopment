package com.example.trashify.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trashify.data.database.ProfilePictureRepository
import com.example.trashify.data.reponse.UserProfileResponse
import com.example.trashify.data.reponse.UserRepo
import kotlinx.coroutines.launch

class AboutViewModel(
    private val userRepo: UserRepo,
    private val profilePictureRepository: ProfilePictureRepository
) : ViewModel() {

    private val _userProfile = MutableLiveData<UserProfileResponse>()
    val userProfile: LiveData<UserProfileResponse> = _userProfile

    fun fetchUserProfile(userId: String) {
        viewModelScope.launch {
            try {
                val profileResponse = profilePictureRepository.getProfilePicture(userId)
                Log.d(TAG, "Fetched user profile: $profileResponse")
                _userProfile.postValue(profileResponse)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch user profile", e)
            }
        }
    }

    fun logout(uid: String) {
        viewModelScope.launch {
            userRepo.logout(uid)
        }
    }

    fun updateProfilePicture(userId: String, imagePath: String, callback: (String) -> Unit) {
        viewModelScope.launch {
            Log.d(TAG, "Attempting to upload profile picture for userId: $userId, imagePath: $imagePath")
            try {
                val response = profilePictureRepository.updateProfilePicture(userId, imagePath)
                Log.d(TAG, "Update profile picture response: $response")
                val newImageUrl = response.userImageProfile
                if (newImageUrl != null) {
                    Log.d(TAG, "New profile image URL: $newImageUrl")
                    _userProfile.value?.let {
                        it.userImageProfile = newImageUrl
                        _userProfile.postValue(it)
                    }
                    callback(newImageUrl)
                } else {
                    Log.e(TAG, "Profile picture update failed: no image URL in response")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to update profile picture", e)
            }
        }
    }

    companion object {
        private const val TAG = "AboutViewModel"
    }
}
