package com.example.myapplication.core.model

import kotlinx.serialization.Serializable

/**
 * Represents a character's Durability.
 *
 * - [value]: The current durability level (which can change in battle).
 * - [modifier]: An optional modifier to nuance the value.
 */
@Serializable
data class DurabilityValue(
    override var value: DurabilityLevel,
    override var modifier: StatModifier? = null
) : StatValueHolder<DurabilityLevel>