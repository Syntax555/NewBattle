package com.example.myapplication.model

/**
 * Represents the origin of a character.
 * This enum enables filtering characters by their source.
 */
enum class Origin(val displayName: String) {
    MARVEL("Marvel Comics"),
    DC("DC Comics"),
    IMAGE("Image Comics"),
    OTHER("Other")
}
