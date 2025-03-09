package com.example.myapplication.model

/**
 * Represents a character's Lifting Strength.
 *
 * - [value]: The current lifting strength level.
 * - [modifier]: An optional modifier to nuance the value.
 */
data class LiftingStrengthValue(
    override var value: LiftingStrengthLevel,
    override var modifier: StatModifier? = null
) : StatValueHolder<LiftingStrengthLevel>