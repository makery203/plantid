package com.example.plantid.utils
import android.content.Context
import android.widget.Toast
import java.io.File

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.getTempImageFile(): File {
    return File(externalCacheDir, "temp_plant_${System.currentTimeMillis()}.jpg")
}
class Extensions {
}