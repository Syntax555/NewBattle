package com.example.myapplication.model

/**
 * A generic tier value used as an overall state for a character.
 *
 * Implements StatValueHolder so it can be updated by stat effects.
 */
data class TierValue(
    override var value: TierLevel,
    override var modifier: StatModifier? = null
) : StatValueHolder<TierLevel>
