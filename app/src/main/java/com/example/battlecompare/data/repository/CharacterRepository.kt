package com.example.battlecompare.data.repository

import android.content.Context
import com.example.battlecompare.core.model.GameCharacter
import com.example.battlecompare.core.util.MemoryCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.IOException

/**
 * Repository interface for character data.
 */
interface CharacterRepository {
    fun getCharactersFlow(): Flow<List<GameCharacter>>
    suspend fun getAllCharacters(): List<GameCharacter>
    suspend fun getCharacterByName(name: String): GameCharacter?
    suspend fun getCharactersByOrigin(originName: String): List<GameCharacter>
    suspend fun refreshCharacters()
}

/**
 * Implementation of the character repository that loads characters from assets.
 * Uses caching and coroutines for efficient loading.
 */
class CharacterRepositoryImpl(
    private val context: Context,
    private val json: Json,
    private val characterCache: MemoryCache<String, GameCharacter>
) : CharacterRepository {

    private val charactersFlow = MutableStateFlow<List<GameCharacter>>(emptyList())
    private var isInitialized = false

    override fun getCharactersFlow(): Flow<List<GameCharacter>> = charactersFlow.asStateFlow()

    override suspend fun getAllCharacters(): List<GameCharacter> {
        if (!isInitialized) {
            refreshCharacters()
        }
        return charactersFlow.value
    }

    override suspend fun getCharacterByName(name: String): GameCharacter? {
        // Try to get from cache first
        return characterCache.get(name.lowercase()) ?: run {
            // If not in cache, try to find in the full list
            getAllCharacters().find { it.name.equals(name, ignoreCase = true) }?.also {
                // Add to cache for future use
                characterCache.put(name.lowercase(), it)
            }
        }
    }

    override suspend fun getCharactersByOrigin(originName: String): List<GameCharacter> {
        return getAllCharacters().filter { it.origin.name.equals(originName, ignoreCase = true) }
    }

    override suspend fun refreshCharacters() = withContext(Dispatchers.IO) {
        try {
            // Load characters in parallel
            val loadedCharacters = loadCharactersFromAssets()

            // Update the cache with new characters
            loadedCharacters.forEach { character ->
                characterCache.put(character.name.lowercase(), character)
            }

            // Update the flow
            charactersFlow.value = loadedCharacters
            isInitialized = true

            Timber.d("Loaded ${loadedCharacters.size} characters")
        } catch (e: Exception) {
            Timber.e(e, "Failed to load characters")
            throw e
        }
    }

    private suspend fun loadCharactersFromAssets(): List<GameCharacter> = withContext(Dispatchers.IO) {
        val characters = mutableListOf<GameCharacter>()

        try {
            // Get list of JSON files from assets
            val assetFiles = context.assets.list("characters") ?: emptyArray()

            // Process only JSON files
            assetFiles.filter { it.endsWith(".json") }
                .map { filename ->
                    try {
                        val jsonData = context.assets.open("characters/$filename").bufferedReader().use { it.readText() }
                        val character = json.decodeFromString<GameCharacter>(jsonData)
                        characters.add(character)
                        Timber.d("Loaded character: ${character.name}")
                    } catch (e: Exception) {
                        Timber.e(e, "Failed to parse character file: $filename")
                        null
                    }
                }
        } catch (e: IOException) {
            Timber.e(e, "Failed to access assets directory")
            throw e
        }

        characters
    }
}