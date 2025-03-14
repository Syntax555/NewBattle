package com.example.battlecompare.core.model

import kotlinx.serialization.Serializable

/**
 * Represents the gender of a character key.
 */
@Serializable
enum class Gender(val label: String) {
    MALE("Male"),
    FEMALE("Female"),
    INDEFINABLE("Indefinable")
}
