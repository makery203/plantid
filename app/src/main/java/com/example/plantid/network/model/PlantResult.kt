package com.example.plantid.network.model

data class PlantResult(
    val score: Double?,
    val species: Species?
)

data class Species(
    val scientificNameWithoutAuthor: String?,
    val commonNames: List<String>?,
    val family: Family?,
    val genus: Genus?
)

data class Family(
    val scientificNameWithoutAuthor: String?
)

data class Genus(
    val scientificNameWithoutAuthor: String?
)
