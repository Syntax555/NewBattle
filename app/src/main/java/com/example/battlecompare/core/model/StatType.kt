package com.example.battlecompare.core.model

import kotlinx.serialization.Serializable

/**
 * Enumerates the types of stats that can be affected by an effect.
 * Now includes an overall TIER in addition to specific stats.
 */
@Serializable
enum class StatType {
    TIER,
    ATTACK_POTENCY,
    SPEED,
    LIFTING_STRENGTH,
    STRIKING_STRENGTH,
    DURABILITY,
    INTELLIGENCE,
    RANGE,
    STAMINA
}
