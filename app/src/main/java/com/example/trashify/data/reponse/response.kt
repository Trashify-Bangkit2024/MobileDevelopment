package com.example.trashify.data.reponse

import com.google.gson.annotations.SerializedName

// Register response
data class RegisterResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,
    @field:SerializedName("message")
    val message: String? = null,
    @field:SerializedName("uid")
    val uid: String? = null,
    @field:SerializedName("userName")
    val userName: String?,
    @field:SerializedName("email")
    val email: String?,
    @field:SerializedName("userImageProfile")
    val userImageProfile: String?
)

// Login response
data class LoginResponse(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("uid") val uid: UserDetail?
)

data class LoginResult(
    val name: String?,
    val uid: String?,
    val email: String?,
    val userImageProfile: String?
)

data class UserDetail(
    @SerializedName("uid") val uid: String,
    @SerializedName("email") val email: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("userImageProfile") val userImageProfile: String
)

data class LogoutRequest(
    @SerializedName("uid") val uid: String
)
// Logout response
data class LogoutResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)

// PROFILE
data class UserProfileResponse(
    @SerializedName("userImageProfile") var userImageProfile: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("uid") val uid: String
)

data class UpdateProfileResponse(
    @SerializedName("userImageProfile") val userImageProfile: String?
)

// Prediction
data class PredictionResponse(
    @SerializedName("label") val label: String,
    @SerializedName("probabilities") val probabilities: List<Double>,
    @SerializedName("description") val description: String,
    @SerializedName("action") val action: String,
    @SerializedName("priceRange") val priceRange: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("uid") val uid: String,
    @SerializedName("timestamp") val timeStamp: TimeStamp
)

data class HistoryPredictionResponse(
    @SerializedName("uid") val uid: String,
    @SerializedName("predictions") val predictions: List<PredictionResponse>
)

data class TimeStamp(
    @SerializedName("_seconds") val seconds: Long,
    @SerializedName("_nanoseconds") val nanoseconds: Int
)


// Error response
data class ErrorResponse(
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("error")
    val error: Boolean
)

