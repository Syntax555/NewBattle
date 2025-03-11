package com.example.myapplication.core.model

import kotlinx.serialization.Serializable

/**
 * Represents a character's Attack Potency.
 *
 * - [value]: The current attack potency level (which can change in battle).
 * - [modifier]: An optional modifier to nuance the value.
 */
@Serializable
data class AttackPotencyValue(
    override var value: AttackPotencyLevel,
    override var modifier: StatModifier? = null
) : StatValueHolder<AttackPotencyLevel>