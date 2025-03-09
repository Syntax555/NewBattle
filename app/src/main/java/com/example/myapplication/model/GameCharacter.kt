package com.example.myapplication.model

import kotlinx.serialization.Serializable

/**
 * Represents a character (e.g., "Hulk") that can have multiple variants (keys).
 *
 * For example, "Hulk" might have keys like "Bruce Banner" or "World War Hulk".
 */
@Serializable
data class GameCharacter(
    val name: String,
    val keys: List<CharacterKey>
)
