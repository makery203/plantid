package com.example.plantid.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantid.network.ApiService
import com.example.plantid.network.exetensions.toPlantDetails
import com.example.plantid.network.response.PlantIdentificationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PlantViewModel(
    private val apiService: ApiService
) : ViewModel() {

    private val _identificationResult = MutableLiveData<PlantIdentificationResponse?>()
    val identificationResult: LiveData<PlantIdentificationResponse?> = _identificationResult

    private val _plantDetails = MutableLiveData<PlantDetails?>()
    val plantDetails: LiveData<PlantDetails?> = _plantDetails

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val API_KEY = "2b10WawZumr4qwsWZ8rzLz9Lye"
        private const val DEFAULT_ORGAN = "leaf"
        private const val DEFAULT_LANGUAGE = "en"
    }

    fun identifyPlant(imageFile: File) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageParts = prepareImageParts(imageFile)
                val organPart = prepareOrganPart()

                val response = apiService.identifyPlant(
                    images = imageParts,
                    organs = organPart,
                    apiKey = API_KEY,
                    includeRelatedImages = true,
                    language = DEFAULT_LANGUAGE
                )

                handleResponse(response)
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun prepareImageParts(imageFile: File): List<MultipartBody.Part> {
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        return listOf(
            MultipartBody.Part.createFormData("images", imageFile.name, requestFile)
        )
    }

    private fun prepareOrganPart() =
        DEFAULT_ORGAN.toRequestBody("text/plain".toMediaTypeOrNull())

    private fun handleResponse(response: PlantIdentificationResponse) {
        if (!response.results.isNullOrEmpty()) {
            val firstResult = response.results.first()
            val imageUrl = firstResult.images.firstOrNull()?.url?.m
            _plantDetails.postValue(firstResult.toPlantDetails(imageUrl))
        } else {
            _errorMessage.postValue("No plant data found.")
        }
    }

    private fun handleError(exception: Exception) {
        val errorMsg = exception.localizedMessage ?: "Unknown error occurred"
        _errorMessage.postValue("Network error: $errorMsg")
        Log.e("PlantViewModel", "Identification error", exception)
    }
}
