package com.example.myapplication.model

/**
 * Represents a character's Stamina.
 *
 * - [value]: The current stamina level.
 * - [modifier]: An optional modifier.
 */
data class StaminaValue(
    override var value: StaminaLevel,
    override var modifier: StatModifier? = null
) : StatValueHolder<StaminaLevel>