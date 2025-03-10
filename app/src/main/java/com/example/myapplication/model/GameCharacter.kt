package com.example.myapplication.model

import kotlinx.serialization.Serializable

/**
 * Represents a character (e.g., "Hulk") that can have multiple variants (keys).
 *
 * Now, origin and gender are properties of the GameCharacter, because every key
 * of that character shares the same origin and gender.
 */
@Serializable
data class GameCharacter(
    val name: String,
    val origin: Origin,
    val gender: Gender,
    val keys: List<CharacterKey>
)
