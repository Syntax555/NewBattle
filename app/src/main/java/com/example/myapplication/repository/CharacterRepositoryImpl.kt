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
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) : CharacterRepository {

    private val charactersCache = MutableStateFlow<List<GameCharacter>>(emptyList())

    override fun getCharactersFlow(): Flow<List<GameCharacter>> = charactersCache.asStateFlow()

    override suspend fun getAllCharacters(): List<GameCharacter> {
        if (charactersCache.value.isEmpty()) {
            refreshCharacters()
        }
        return charactersCache.value
    }

    override suspend fun getCharacterByName(name: String): GameCharacter? {
        return getAllCharacters().find { it.name.equals(name, ignoreCase = true) }
    }

    override suspend fun refreshCharacters() = withContext(Dispatchers.IO) {
        try {
            val loadedCharacters = loadCharactersFromAssets()
            charactersCache.value = loadedCharacters
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
            val assetFiles = context.assets.list("") ?: emptyArray()

            // Process only JSON files
            assetFiles.filter { it.endsWith(".json") }.forEach { filename ->
                try {
                    val jsonData = context.assets.open(filename).bufferedReader().use { it.readText() }
                    val character = json.decodeFromString<GameCharacter>(jsonData)
                    characters.add(character)
                    Timber.d("Loaded character: ${character.name}")
                } catch (e: Exception) {
                    Timber.e(e, "Failed to parse character file: $filename")
                }
            }
        } catch (e: IOException) {
            Timber.e(e, "Failed to access assets directory")
            throw e
        }

        characters
    }
}