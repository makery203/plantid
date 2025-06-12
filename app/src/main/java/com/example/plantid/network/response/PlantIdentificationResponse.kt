// com/example/plantid/network/model/PlantIdentificationResponse.kt
package com.example.plantid.network.response

data class PlantIdentificationResponse(
    val results: List<PlantResult>
)

data class PlantResult(
    val score: Double?,
    val species: Species,
    val images: List<PlantImage>
)

data class Species(
    val scientificNameWithoutAuthor: String?,
    val commonNames: List<String>?,
    val vegetativeDescription: String?,
    val family: Family?,
    val genus: Genus?,
    val links: Links?
)

data class Family(val scientificName: String?)
data class Genus(val scientificName: String?)
data class Links(val wikipedia: String?)
data class PlantImage(val url: PlantImageUrl)
data class PlantImageUrl(val m: String?)
