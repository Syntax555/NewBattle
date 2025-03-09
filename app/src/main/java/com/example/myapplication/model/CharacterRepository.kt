package com.example.myapplication.model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * A repository for loading GameCharacter data from external JSON.
 *
 * This data‑driven approach lets you maintain many characters without hard‑coding them.
 */
object CharacterRepository {
    // Configure the JSON parser; ignore unknown keys to allow schema evolution.
    private val jsonParser = Json { ignoreUnknownKeys = true }

    /**
     * Loads a GameCharacter from a JSON string.
     * In a full implementation, you might read the JSON from a file or network.
     */
    fun loadCharacter(jsonData: String): GameCharacter {
        return jsonParser.decodeFromString(jsonData)
    }
}
