package com.example.myapplication.model

/**
 * Represents a character's Striking Strength.
 *
 * - [value]: The current striking strength level (which can change in battle).
 * - [modifier]: An optional modifier to nuance the value.
 */
data class StrikingStrengthValue(
    override var value: StrikingStrengthLevel,
    override var modifier: StatModifier? = null
) : StatValueHolder<StrikingStrengthLevel>
