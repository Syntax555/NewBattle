package com.example.battlecompare.core.model

import kotlinx.serialization.Serializable

/**
 * Generic implementation for any statistic value with its modifier.
 * This replaces multiple specific classes like AttackPotencyValue, SpeedValue, etc.
 */
@Serializable
data class StatValue<T : Enum<T>>(
    var value: T,
    var modifier: StatModifier? = null
)

/**
 * Additional modifiers to nuance a stat value.
 */
@Serializable
enum class StatModifier(val symbol: String, val bonusValue: Int) {
    PLUS("+", 4),
    AT_LEAST("at least", 2),
    AT_MOST("at most", 1),
    LIKELY("likely", 3),
    POSSIBLY("possibly", 2),
    HIGHER("higher", 2),
    LOWER("lower", 0),
    VARIES("varies", 2)
}

/**
 * Extension function to create a stat value from an enum value.
 * Makes code more concise when creating stat values.
 */
fun <T : Enum<T>> T.toStatValue(modifier: StatModifier? = null): StatValue<T> {
    return StatValue(this, modifier)
}