// CharacterRepositoryImpl.kt
package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.model.GameCharacter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of the CharacterRepositoryInterface.
 * Caches character data in memory and loads from assets when needed.
 */
@Singleton
class CharacterRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : CharacterRepositoryInterface {

    private val jsonParser = Json { ignoreUnknownKeys = true }
    private val charactersCache = MutableStateFlow<List<GameCharacter>>(emptyList())

    // Lazy initialization to avoid loading on app startup
    private val characters: Flow<List<GameCharacter>> = charactersCache.asStateFlow()

    override fun getCharactersFlow(): Flow<List<GameCharacter>> = characters

    override suspend fun getAllCharacters(): List<GameCharacter> {
        // If cache is empty, load data
        if (charactersCache.value.isEmpty()) {
            refreshCharacters()
        }
        return charactersCache.value
    }

    override suspend fun getCharacterByName(name: String): GameCharacter? {
        return getAllCharacters().find { it.name == name }
    }

    override suspend fun refreshCharacters() = withContext(Dispatchers.IO) {
        try {
            val loadedCharacters = loadCharactersFromAssets()
            charactersCache.value = loadedCharacters
        } catch (e: Exception) {
            // Log error and potentially report to analytics
            throw e
        }
    }

    private suspend fun loadCharactersFromAssets(): List<GameCharacter> = withContext(Dispatchers.IO) {
        val characters = mutableListOf<GameCharacter>()
        // Get list of asset files
        val assetFiles = context.assets.list("") ?: arrayOf()

        // Process only JSON files
        assetFiles.filter { it.endsWith(".json") }.forEach { filename ->
            try {
                val jsonData = context.assets.open(filename).bufferedReader().use { it.readText() }
                val character = jsonParser.decodeFromString<GameCharacter>(jsonData)
                characters.add(character)
            } catch (e: Exception) {
                // Log specific error but continue processing other files
            }
        }
        characters
    }
}
