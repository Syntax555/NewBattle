package com.example.myapplication.model

import kotlinx.serialization.Serializable

/**
 * Represents an effect that modifies a specific stat.
 *
 * This sealed class allows for additional effect types in the future.
 */
@Serializable
sealed class StatEffect {
    abstract val statType: StatType
    abstract val change: Double
    abstract val target: TargetType
    abstract val description: String?
    abstract val modifier: StatModifier?
    /**
     * An optional tag to identify the source/type of effect.
     * For example, a power and a corresponding resistance might share the same tag.
     */
    abstract val abilityTag: String?

    @Serializable
    data class SimpleEffect(
        override val statType: StatType,
        override val change: Double,
        override val target: TargetType = TargetType.SELF,
        override val description: String? = null,
        override val modifier: StatModifier? = null,
        override val abilityTag: String? = null
    ) : StatEffect()
}