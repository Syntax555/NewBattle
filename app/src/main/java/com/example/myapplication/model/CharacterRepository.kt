// CharacterRepository.kt (updated)
package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.model.GameCharacter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/**
 * Repository for accessing character data.
 */
class CharacterRepository(private val context: Context) {

    private val jsonParser = Json { ignoreUnknownKeys = true }
    private val charactersCache = MutableStateFlow<List<GameCharacter>>(emptyList())

    // Public API
    val characters: Flow<List<GameCharacter>> = charactersCache.asStateFlow()

    /**
     * Load all characters from assets.
     */
    suspend fun loadAllCharacters(): List<GameCharacter> {
        if (charactersCache.value.isEmpty()) {
            refreshCharacters()
        }
        return charactersCache.value
    }

    /**
     * Force refresh of character data.
     */
    suspend fun refreshCharacters() = withContext(Dispatchers.IO) {
        try {
            val loadedCharacters = loadCharactersFromAssets()
            charactersCache.value = loadedCharacters
        } catch (e: Exception) {
            // Log error
            throw e
        }
    }

    /**
     * Load characters from assets.
     */
    private suspend fun loadCharactersFromAssets(): List<GameCharacter> = withContext(Dispatchers.IO) {
        val characters = mutableListOf<GameCharacter>()
        val assetFiles = context.assets.list("") ?: arrayOf()

        assetFiles.filter { it.endsWith(".json") }.forEach { filename ->
            try {
                val jsonData = context.assets.open(filename).bufferedReader().use { it.readText() }
                val character = jsonParser.decodeFromString<GameCharacter>(jsonData)
                characters.add(character)
            } catch (e: Exception) {
                // Log error
            }
        }
        characters
    }
}