package com.example.myapplication.model

import kotlinx.serialization.Serializable

/**
 * A generic tier value used as an overall state for a character.
 *
 * Implements StatValueHolder so it can be updated by stat effects.
 */
@Serializable
data class TierValue(
    override var value: TierLevel,
    override var modifier: StatModifier? = null
) : StatValueHolder<TierLevel>
