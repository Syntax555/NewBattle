package com.example.myapplication.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class for loading and caching images from assets.
 */
@Singleton
class ImageLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageCache: MemoryCache<String, ImageBitmap>
) {
    /**
     * Load an image from assets with caching.
     */
    suspend fun loadImageFromAssets(imagePath: String): ImageBitmap? {
        return imageCache.getOrPut(imagePath) {
            loadImageFromAssetsInternal(imagePath)
        }
    }

    /**
     * Internal method to load an image from assets without caching.
     */
    private suspend fun loadImageFromAssetsInternal(imagePath: String): ImageBitmap = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.assets.open(imagePath)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap.asImageBitmap()
        } catch (e: IOException) {
            throw IOException("Failed to load image: $imagePath", e)
        }
    }

    /**
     * Preload a list of images in the background.
     */
    suspend fun preloadImages(imagePaths: List<String>) = withContext(Dispatchers.IO) {
        imagePaths.forEach { path ->
            try {
                loadImageFromAssets(path)
            } catch (e: Exception) {
                // Log error but continue preloading
            }
        }
    }

    /**
     * Clear the image cache.
     */
    suspend fun clearCache() {
        imageCache.clear()
    }
}