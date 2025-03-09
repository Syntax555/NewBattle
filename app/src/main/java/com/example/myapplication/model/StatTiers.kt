package com.example.myapplication.model

/**
 * Encapsulates all tiered stat values for a character.
 *
 * Each stat is modeled as a mutable value (which may change during battle).
 */
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
)
