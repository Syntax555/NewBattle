package com.example.myapplication.core.model

import kotlinx.serialization.Serializable

/**
 * Represents a power that a character key can have.
 *
 * Powers can modify stats by providing one or more stat effects.
 * They can also disable or counteract enemy powers or equipment by specifying ability tags.
 *
 * @property name The name of the power.
 * @property description A description of the power.
 * @property statEffects A list of stat effects that this power applies.
 * @property disables A set of ability tags that this power disables.
 */
@Serializable
data class Power(
    val name: String,
    val description: String,
    override val statEffects: List<StatEffect> = emptyList(),
    override val disables: Set<String> = emptySet()
) : StatEffectProvider, DisableProvider
