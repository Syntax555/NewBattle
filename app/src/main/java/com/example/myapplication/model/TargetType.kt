package com.example.myapplication.model

/**
 * Represents the target of a stat effect.
 *
 * - SELF: The effect applies to the character possessing the ability.
 * - ENEMY: The effect applies to enemy characters.
 * - GLOBAL: The effect applies to all characters or the entire battlefield.
 */
enum class TargetType {
    SELF,
    ENEMY,
    GLOBAL
}
