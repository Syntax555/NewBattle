package com.example.myapplication.core.model

import kotlinx.serialization.Serializable

/**
 * Represents the target of a stat effect.
 *
 * - SELF: The effect applies to the character possessing the ability.
 * - ENEMY: The effect applies to enemy characters.
 * - GLOBAL: The effect applies to all characters or the entire battlefield.
 */
@Serializable
enum class TargetType {
    SELF,
    ENEMY,
    GLOBAL
}
