// CharacterLoader.kt
package com.example.myapplication.util

import android.content.Context
import com.example.myapplication.model.GameCharacter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class for efficient character loading with caching.
 */
@Singleton
class CharacterLoader @Inject constructor(
    private val context: Context,
    private val json: Json
) {
    // Thread-safe cache of loaded characters
    private val characterCache = ConcurrentHashMap<String, GameCharacter>()

    // List of available character filenames for lazy loading
    private var availableCharacterFiles: List<String>? = null

    /**
     * Gets a list of available character filenames without loading their content.
     */
    suspend fun getAvailableCharacterFiles(): List<String> = withContext(Dispatchers.IO) {
        if (availableCharacterFiles == null) {
            val assetFiles = context.assets.list("") ?: arrayOf()
            availableCharacterFiles = assetFiles.filter { it.endsWith(".json") }
        }
        availableCharacterFiles!!
    }

    /**
     * Loads a character by filename, using the cache if available.
     */
    suspend fun loadCharacter(filename: String): GameCharacter? = withContext(Dispatchers.IO) {
        // Return from cache if available
        characterCache[filename]?.let { return@withContext it }

        try {
            // Load and parse character
            val jsonData = context.assets.open(filename).bufferedReader().use { it.readText() }
            val character = json.decodeFromString<GameCharacter>(jsonData)

            // Store in cache
            characterCache[filename] = character
            character
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Preloads all characters in the background.
     * Useful for initial app load or when entering the selection screen.
     */
    suspend fun preloadAllCharacters() {
        getAvailableCharacterFiles().forEach { filename ->
            loadCharacter(filename)
        }
    }

    /**
     * Clears the character cache to free memory.
     */
    fun clearCache() {
        characterCache.clear()
    }
}