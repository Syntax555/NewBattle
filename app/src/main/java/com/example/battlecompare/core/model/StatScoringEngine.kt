package com.example.battlecompare.core.model

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