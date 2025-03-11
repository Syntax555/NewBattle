package com.example.myapplication.core.model

import kotlinx.serialization.Serializable

/**
 * Represents an attack that a character can perform.
 *
 * Attacks can modify stats by applying one or more stat effects.
 * They can also disable or counteract enemy powers or equipment by specifying ability tags.
 *
 * @property name The name of the attack.
 * @property description A description of the attack.
 * @property statEffects A list of stat effects produced by the attack.
 * @property disables A set of ability tags that this attack disables.
 */
@Serializable
data class Attack(
    val name: String,
    val description: String,
    override val statEffects: List<StatEffect> = emptyList(),
    override val disables: Set<String> = emptySet()
) : StatEffectProvider, DisableProvider
