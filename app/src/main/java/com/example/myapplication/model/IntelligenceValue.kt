package com.example.myapplication.model

/**
 * Represents a character's Intelligence.
 *
 * - [value]: The current intelligence level.
 * - [modifier]: An optional modifier.
 */
data class IntelligenceValue(
    override var value: IntelligenceLevel,
    override var modifier: StatModifier? = null
) : StatValueHolder<IntelligenceLevel>