package com.example.battlecompare.core.model

import kotlinx.serialization.Serializable

/**
 * Encapsulates all tiered stat values for a character using the generic StatValue implementation.
 */
@Serializable
data class StatTiers(
    var tier: StatValue<TierLevel>,
    var attackPotency: StatValue<AttackPotencyLevel>,
    var speed: StatValue<SpeedLevel>,
    var liftingStrength: StatValue<LiftingStrengthLevel>,
    var strikingStrength: StatValue<StrikingStrengthLevel>,
    var durability: StatValue<DurabilityLevel>,
    var intelligence: StatValue<IntelligenceLevel>,
    var range: StatValue<RangeLevel>,
    var stamina: StatValue<StaminaLevel>
) {
    /**
     * A computed property that returns the total score for the character's stats.
     * This score is automatically updated whenever an individual stat changes.
     */
    val totalScore: Int
        get() = StatScoringEngine.calculateTotalScore(this)

    /**
     * Factory method to create a default StatTiers instance with minimum values.
     */
    companion object {
        fun createDefault(): StatTiers {
            return StatTiers(
                tier = TierLevel._10C.toStatValue(),
                attackPotency = AttackPotencyLevel.BELOW_AVERAGE_HUMAN.toStatValue(),
                speed = SpeedLevel.BELOW_AVERAGE_HUMAN.toStatValue(),
                liftingStrength = LiftingStrengthLevel.BELOW_AVERAGE_HUMAN.toStatValue(),
                strikingStrength = StrikingStrengthLevel.BELOW_AVERAGE_HUMAN.toStatValue(),
                durability = DurabilityLevel.BELOW_AVERAGE_HUMAN.toStatValue(),
                intelligence = IntelligenceLevel.BELOW_AVERAGE.toStatValue(),
                range = RangeLevel.BELOW_STANDARD.toStatValue(),
                stamina = StaminaLevel.BELOW_AVERAGE.toStatValue()
            )
        }
    }
}