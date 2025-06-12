// PlantViewModelFactory.kt
package com.example.plantid.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plantid.network.ApiService

class PlantViewModelFactory(
    private val apiService: ApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlantViewModel::class.java)) {
            return PlantViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

