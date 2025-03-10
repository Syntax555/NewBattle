package com.example.myapplication.model

import kotlinx.serialization.Serializable

/**
 * Represents the levels for a character's Stamina.
 *
 * For example:
 * - Unknown
 * - Below Average
 * - Average
 * - Athletic
 * - Peak Human
 * - Superhuman
 * - Infinite
 * - Inapplicable
 */
@Serializable
enum class StaminaLevel(val label: String) {
    UNKNOWN("Unknown"),
    BELOW_AVERAGE("Below Average"),
    AVERAGE("Average"),
    ATHLETIC("Athletic"),
    PEAK_HUMAN("Peak Human"),
    SUPERHUMAN("Superhuman"),
    INFINITE("Infinite"),
    INAPPLICABLE("Inapplicable")
}
