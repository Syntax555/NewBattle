// CharacterRepositoryInterface.kt
package com.example.myapplication.repository

import com.example.myapplication.model.GameCharacter
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for character data operations.
 * This abstraction allows for different implementations (e.g., local, remote).
 */
interface CharacterRepositoryInterface {
    /**
     * Get all characters as a Flow.
     * Using Flow provides reactive updates when data changes.
     */
    fun getCharactersFlow(): Flow<List<GameCharacter>>

    /**
     * Get all characters as a suspending function.
     * Useful for one-time data loading.
     */
    suspend fun getAllCharacters(): List<GameCharacter>

    /**
     * Get a character by name.
     */
    suspend fun getCharacterByName(name: String): GameCharacter?

    /**
     * Refresh character data from source.
     */
    suspend fun refreshCharacters()
}