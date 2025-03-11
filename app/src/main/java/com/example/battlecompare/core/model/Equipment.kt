package com.example.battlecompare.core.model

import kotlinx.serialization.Serializable

/**
 * Represents an equipment item associated with a character key.
 *
 * Equipment can change stats—either boosting the wearer's abilities or affecting enemies.
 *
 * @property name The name of the equipment.
 * @property description A description of the equipment.
 * @property statEffects A list of stat effects that this equipment provides.
 * @property isOptional If true, the equipment is optional and can later be activated/deactivated by the user.
 *                     Standard equipment should have this set to false.
 * @property enabled Whether the equipment is currently active. For standard equipment, this is always true.
 * @property disables A set of ability tags that this equipment disables.
 */
@Serializable
data class Equipment(
    val name: String,
    val description: String,
    override val statEffects: List<StatEffect> = emptyList(),
    val isOptional: Boolean = false,
    val enabled: Boolean = !isOptional,
    override val disables: Set<String> = emptySet()
) : StatEffectProvider, DisableProvider