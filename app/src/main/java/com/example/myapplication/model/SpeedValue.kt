package com.example.myapplication.model

/**
 * Represents a character's Speed.
 *
 * - [value]: The current speed level.
 * - [modifier]: An optional modifier to nuance the value.
 */
data class SpeedValue(
    override var value: SpeedLevel,
    override var modifier: StatModifier? = null
) : StatValueHolder<SpeedLevel>
