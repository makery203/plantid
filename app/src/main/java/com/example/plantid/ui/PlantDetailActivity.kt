package com.example.plantid.ui
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.plantid.R
import com.example.plantid.databinding.ActivityPlantDetailBinding
import com.example.plantid.domain.model.PlantDetailsModel

class PlantDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val plant = intent.getParcelableExtra<PlantDetailsModel>("plant")
        if (plant != null) {
            displayPlantDetails(plant)
        } else {
            binding.tvScientificName.text = getString(R.string.no_plant_data)
        }
    }

    @SuppressLint("DefaultLocale")
    private fun displayPlantDetails(plant: PlantDetailsModel) {
        binding.tvScientificName.text = "Scientific Name: ${plant.scientificName}"
        binding.tvCommonNames.text = "Common Names: ${plant.commonNames?.joinToString()}"
        binding.tvFamily.text = "Family: ${plant.family}"
        binding.tvGenus.text = "Genus: ${plant.genus}"
        binding.tvScore.text = "Confidence Score: ${String.format("%.2f", plant.confidenceScore ?: 0f)}"
        binding.tvWiki.text = "More Info: ${plant.wikiUrl ?: "N/A"}"

        Glide.with(this)
            .load(plant.imageUrl)
            .placeholder(R.drawable.placeholder_plant)
            .into(binding.ivPlantImage)
    }
}