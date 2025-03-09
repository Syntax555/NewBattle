package com.example.myapplication.model

/**
 * Represents a character's Durability.
 *
 * - [value]: The current durability level (which can change in battle).
 * - [modifier]: An optional modifier to nuance the value.
 */
data class DurabilityValue(
    override var value: DurabilityLevel,
    override var modifier: StatModifier? = null
) : StatValueHolder<DurabilityLevel>