package com.example.plantid.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlantDetailsModel(
    val scientificName: String,
    val commonNames: List<String> = emptyList(),
    val confidenceScore: Float? = null,
    val imageUrl: String? = null,
    val description: String? = null,
    val family: String? = null,
    val genus: String? = null,
    val wikiUrl: String? = null
) : Parcelable {
    fun getDisplayName(): String {
        return if (commonNames.isNotEmpty()) {
            "${commonNames.first()} ($scientificName)"
        } else {
            scientificName
        }
    }
}
// NO OTHER CLASS DECLARATIONS IN THIS FILE