package com.example.trashify.data.api

import com.example.trashify.data.reponse.HistoryPredictionResponse
import com.example.trashify.data.reponse.LoginResponse
import com.example.trashify.data.reponse.LogoutRequest
import com.example.trashify.data.reponse.LogoutResponse
import com.example.trashify.data.reponse.PredictionResponse
import com.example.trashify.data.reponse.RegisterResponse
import com.example.trashify.data.reponse.UpdateProfileResponse
import com.example.trashify.data.reponse.UserProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @Multipart
    @POST("auth/register")
    suspend fun register(
        @Part("userName") userName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirmPassword") confirmPassword: RequestBody,
        @Part userImageProfile: MultipartBody.Part?
    ): RegisterResponse

    @Multipart
    @POST("user/update")
    suspend fun updateUserProfile(
        @Part("uid") uid: RequestBody,
        @Part userImageProfile: MultipartBody.Part
    ): UpdateProfileResponse

    @GET("user/profile/{userId}")
    suspend fun getUserProfile(@Path("userId") userId: String): UserProfileResponse

    @POST("auth/login")
    suspend fun login(
        @Body body: Map<String, String>
    ): LoginResponse

    @POST("auth/logout")
    suspend fun logout(
        @Body logoutRequest: LogoutRequest
    ): LogoutResponse

    @Multipart
    @POST("user/predict")
    suspend fun postPrediction(
        @Part("uid") uid: RequestBody,
        @Part image: MultipartBody.Part
    ): PredictionResponse

    @GET("user/predict/{uid}")
    suspend fun getHistoryPrediction(@Path("uid") uid: String): HistoryPredictionResponse

}
