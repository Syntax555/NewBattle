// StatValue.kt
package com.example.myapplication.model

/**
 * Generic implementation of StatValueHolder that can be used for all stat types.
 * This reduces code duplication across multiple stat value classes.
 */
data class StatValue<T : Enum<T>>(
    override var value: T,
    override var modifier: StatModifier? = null
) : StatValueHolder<T>

/**
 * Extension function to create a stat value from an enum value and optional modifier.
 * Makes code more concise when creating stat values.
 */
inline fun <reified T : Enum<T>> T.toStatValue(modifier: StatModifier? = null): StatValue<T> {
    return StatValue(this, modifier)
}

/**
 * Unified StatTiers class that uses the generic StatValue implementation.
 */
data class UnifiedStatTiers(
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
     * Factory method to create a default StatTiers instance with minimum values.
     */
    companion object {
        fun createDefault(): UnifiedStatTiers {
            return UnifiedStatTiers(
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

        /**
         * Factory method to create a StatTiers instance from the original StatTiers.
         */
        fun fromOriginal(original: StatTiers): UnifiedStatTiers {
            return UnifiedStatTiers(
                tier = StatValue(original.tier.value, original.tier.modifier),
                attackPotency = StatValue(original.attackPotency.value, original.attackPotency.modifier),
                speed = StatValue(original.speed.value, original.speed.modifier),
                liftingStrength = StatValue(original.liftingStrength.value, original.liftingStrength.modifier),
                strikingStrength = StatValue(original.strikingStrength.value, original.strikingStrength.modifier),
                durability = StatValue(original.durability.value, original.durability.modifier),
                intelligence = StatValue(original.intelligence.value, original.intelligence.modifier),
                range = StatValue(original.range.value, original.range.modifier),
                stamina = StatValue(original.stamina.value, original.stamina.modifier)
            )
        }
    }

    /**
     * Convert back to the original StatTiers class for backward compatibility.
     */
    fun toOriginal(): StatTiers {
        return StatTiers(
            tier = TierValue(tier.value, tier.modifier),
            attackPotency = AttackPotencyValue(attackPotency.value, attackPotency.modifier),
            speed = SpeedValue(speed.value, speed.modifier),
            liftingStrength = LiftingStrengthValue(liftingStrength.value, liftingStrength.modifier),
            strikingStrength = StrikingStrengthValue(strikingStrength.value, strikingStrength.modifier),
            durability = DurabilityValue(durability.value, durability.modifier),
            intelligence = IntelligenceValue(intelligence.value, intelligence.modifier),
            range = RangeValue(range.value, range.modifier),
            stamina = StaminaValue(stamina.value, stamina.modifier)
        )
    }

    /**
     * Calculate the total score using the optimized scoring engine.
     */
    val totalScore: Int
        get() = with(OptimizedStatScoringEngine) {
            tier.calculateScoreOptimized() +
                    attackPotency.calculateScoreOptimized() +
                    speed.calculateScoreOptimized() +
                    liftingStrength.calculateScoreOptimized() +
                    strikingStrength.calculateScoreOptimized() +
                    durability.calculateScoreOptimized() +
                    intelligence.calculateScoreOptimized() +
                    range.calculateScoreOptimized() +
                    stamina.calculateScoreOptimized()
        }
}