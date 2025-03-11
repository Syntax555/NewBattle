package com.example.battlecompare.core.model

import kotlinx.serialization.Serializable

/**
 * Generic implementation for any statistic value with its modifier.
 * This replaces multiple specific classes like AttackPotencyValue, SpeedValue, etc.
 */
@Serializable
data class StatValue<T : Enum<T>>(
    var value: T,
    var modifier: StatModifier? = null
)

/**
 * Interface for enums that represent stat levels.
 * Provides common properties for all stat levels.
 */
interface StatLevel {
    val label: String
}

/**
 * Additional modifiers to nuance a stat value.
 */
@Serializable
enum class StatModifier(val symbol: String, val bonusValue: Int) {
    PLUS("+", 4),
    AT_LEAST("at least", 2),
    AT_MOST("at most", 1),
    LIKELY("likely", 3),
    POSSIBLY("possibly", 2),
    HIGHER("higher", 2),
    LOWER("lower", 0),
    VARIES("varies", 2)
}

/**
 * Extension function to create a stat value from an enum value.
 * Makes code more concise when creating stat values.
 */
fun <T : Enum<T>> T.toStatValue(modifier: StatModifier? = null): StatValue<T> {
    return StatValue(this, modifier)
}

/**
 * Consolidated representation of all character stats.
 * Uses the generic StatValue implementation for all stats.
 */
@Serializable
data class StatTiers(
    val tier: StatValue<TierLevel>,
    val attackPotency: StatValue<AttackPotencyLevel>,
    val speed: StatValue<SpeedLevel>,
    val liftingStrength: StatValue<LiftingStrengthLevel>,
    val strikingStrength: StatValue<StrikingStrengthLevel>,
    val durability: StatValue<DurabilityLevel>,
    val intelligence: StatValue<IntelligenceLevel>,
    val range: StatValue<RangeLevel>,
    val stamina: StatValue<StaminaLevel>
) {
    /**
     * Calculate the total score using the optimized scoring engine.
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

/**
 * Optimized stat calculation engine with caching for better performance.
 */
object StatScoringEngine {
    // Configuration constants
    const val BASE_MULTIPLIER: Int = 5
    const val BASE_OFFSET: Int = 1

    // Cache for enum ordinal values to avoid repeated reflection
    private val ordinalCache = mutableMapOf<Enum<*>, Int>()

    // Cache for modifier bonus values
    private val modifierBonusCache = mutableMapOf<StatModifier?, Int>()

    // Cache for total scores
    private val scoreCache = mutableMapOf<StatTiers, Int>()

    /**
     * Calculate score for a stat value with caching.
     */
    fun <T : Enum<T>> calculateScore(stat: StatValue<T>): Int {
        val ordinal = ordinalCache.getOrPut(stat.value) { stat.value.ordinal }
        val bonus = modifierBonusCache.getOrPut(stat.modifier) { stat.modifier?.bonusValue ?: 2 }

        return (ordinal * BASE_MULTIPLIER) + bonus + BASE_OFFSET
    }

    /**
     * Calculate total score for a character's stat tiers with caching.
     */
    fun calculateTotalScore(statTiers: StatTiers): Int {
        return scoreCache.getOrPut(statTiers) {
            calculateScore(statTiers.tier) +
                    calculateScore(statTiers.attackPotency) +
                    calculateScore(statTiers.speed) +
                    calculateScore(statTiers.liftingStrength) +
                    calculateScore(statTiers.strikingStrength) +
                    calculateScore(statTiers.durability) +
                    calculateScore(statTiers.intelligence) +
                    calculateScore(statTiers.range) +
                    calculateScore(statTiers.stamina)
        }
    }

    /**
     * Apply a stat effect to a stat value.
     */
    fun <T : Enum<T>> applyEffect(
        stat: StatValue<T>,
        change: Int,
        modifier: StatModifier? = null
    ) {
        // Get the enum constants using reflection
        val enumConstants = stat.value.javaClass.enumConstants

        // Calculate new ordinal with bounds checking
        val currentOrdinal = ordinalCache.getOrPut(stat.value) { stat.value.ordinal }
        val newOrdinal = (currentOrdinal + change).coerceIn(0, enumConstants.size - 1)

        // Update the stat value
        stat.value = enumConstants[newOrdinal]
        stat.modifier = modifier

        // Update cache
        ordinalCache[stat.value] = newOrdinal

        // Invalidate score cache for this stat
        scoreCache.clear()
    }

    /**
     * Clear all caches.
     */
    fun clearCaches() {
        ordinalCache.clear()
        modifierBonusCache.clear()
        scoreCache.clear()
    }
}