// ApiService.kt
package com.example.plantid.network

import com.example.plantid.network.response.PlantIdentificationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("identify")
    suspend fun identifyPlant(
        @Part images: List<MultipartBody.Part>,
        @Part("organs") organs: RequestBody,
        @Query("api-key") apiKey: String,
        @Query("include-related-images") includeRelatedImages: Boolean,
        @Query("lang") language: String
    ): PlantIdentificationResponse
}
