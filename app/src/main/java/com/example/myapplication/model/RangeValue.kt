package com.example.myapplication.model

import kotlinx.serialization.Serializable

/**
 * Represents a character's Range.
 *
 * - [value]: The current range level (which can change during battle).
 * - [modifier]: An optional modifier to nuance the value.
 */
@Serializable
data class RangeValue(
    override var value: RangeLevel,
    override var modifier: StatModifier? = null
) : StatValueHolder<RangeLevel>