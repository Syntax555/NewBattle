package com.example.myapplication.core.model

import kotlinx.serialization.Serializable

/**
 * Represents a character's Lifting Strength.
 *
 * - [value]: The current lifting strength level.
 * - [modifier]: An optional modifier to nuance the value.
 */
@Serializable
data class LiftingStrengthValue(
    override var value: LiftingStrengthLevel,
    override var modifier: StatModifier? = null
) : StatValueHolder<LiftingStrengthLevel>