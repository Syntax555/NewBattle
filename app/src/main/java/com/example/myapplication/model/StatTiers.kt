package com.example.myapplication.model

import kotlinx.serialization.Serializable

/**
 * Encapsulates all tiered stat values for a character.
 *
 * Each stat is modeled as a mutable value (which may change during battle).
 */
@Serializable
data class StatTiers(
    var tier: TierValue,
    var attackPotency: AttackPotencyValue,
    var speed: SpeedValue,
    var liftingStrength: LiftingStrengthValue,
    var strikingStrength: StrikingStrengthValue,
    var durability: DurabilityValue,
    var intelligence: IntelligenceValue,
    var range: RangeValue,
    var stamina: StaminaValue
) {
    /**
     * A computed property that returns the total score for the character's stats.
     * This score is automatically updated whenever an individual stat changes.
     */
    val totalScore: Int
        get() = calculateTotalScore()
}