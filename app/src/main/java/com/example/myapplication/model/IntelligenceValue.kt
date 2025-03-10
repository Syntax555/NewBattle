package com.example.myapplication.model

import kotlinx.serialization.Serializable

/**
 * Represents a character's Intelligence.
 *
 * - [value]: The current intelligence level.
 * - [modifier]: An optional modifier.
 */
@Serializable
data class IntelligenceValue(
    override var value: IntelligenceLevel,
    override var modifier: StatModifier? = null
) : StatValueHolder<IntelligenceLevel>