// domain/model/PlantDetails.kt
package com.example.plantid.model

data class PlantDetails(
    val scientificName: String,
    val commonNames: List<String> = emptyList(),
    val confidenceScore: Float? = null,
    val imageUrl: String? = null,
    val description: String? = null,
    val family: String? = null,
    val genus: String? = null,
    val wikiUrl: String? = null
) {
    // Helper function to get display name
    fun getDisplayName(): String {
        return if (commonNames.isNotEmpty()) {
            "${commonNames.first()} ($scientificName)"
        } else {
            scientificName
        }
    }
}