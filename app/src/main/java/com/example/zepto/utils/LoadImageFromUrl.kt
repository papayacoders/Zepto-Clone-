package com.example.zepto.utils


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

/**
 * Loads an image from a URL and returns it as an ImageBitmap
 */
@Composable
fun loadImageFromUrl(url: String): ImageBitmap? {
    var bitmap by remember(url) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(url) {
        bitmap = fetchImageFromUrl(url)?.asImageBitmap()
    }

    return bitmap
}

/**
 * Fetches an image from a URL and returns it as a Bitmap
 */
private suspend fun fetchImageFromUrl(url: String): Bitmap? = withContext(Dispatchers.IO) {
    try {
        val connection = URL(url).openConnection()
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.connect()

        val inputStream = connection.getInputStream()
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        bitmap
    } catch (e: Exception) {
        null
    }
}