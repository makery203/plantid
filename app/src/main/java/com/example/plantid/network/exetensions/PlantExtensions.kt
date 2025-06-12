package com.example.plantid.network.exetensions

import com.example.plantid.model.PlantDetails

fun Result.toPlantDetails(imageUrl: String? = null): PlantDetails {
    return PlantDetails(
        scientificName = species?.scientificNameWithoutAuthor ?: "N/A",
        commonNames = species?.commonNames ?: emptyList(),
        confidenceScore = score?.toFloat() ?: 0f,
        imageUrl = imageUrl,
        family = species?.family?.scientificNameWithoutAuthor ?: "N/A",
        genus = species?.genus?.scientificNameWithoutAuthor ?: "N/A"
        // Add wikiUrl or description if available
    )
}
