package com.example.myapplication.core.model

import kotlinx.serialization.Serializable

/**
 * Represents a character's Stamina.
 *
 * - [value]: The current stamina level.
 * - [modifier]: An optional modifier.
 */
@Serializable
data class StaminaValue(
    override var value: StaminaLevel,
    override var modifier: StatModifier? = null
) : StatValueHolder<StaminaLevel>