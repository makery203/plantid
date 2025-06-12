import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PlantNetApiService {

    @POST("identify")
    suspend fun identifyPlant(
        @Body requestBody: PlantIdentificationRequest
    ): Response<PlantIdentificationResponse>
}

// Sample request and response classes
data class PlantIdentificationRequest(
    val images: List<String>,   // e.g. base64 encoded images or URLs
    val organs: List<String>    // parts of the plant like "leaf", "flower"
)

data class PlantIdentificationResponse(
    val results: List<PlantResult>
)

data class PlantResult(
    val species: Species
)

data class Species(
    val scientificNameWithoutAuthor: String,
    val commonNames: List<String>
)
