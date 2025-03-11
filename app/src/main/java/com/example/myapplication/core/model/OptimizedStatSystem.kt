package com.example.myapplication.core.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A consolidated and optimized stats system that improves performance by:
 * 1. Using a cache for common values
 * 2. Reducing redundant operations
 * 3. Consolidating scoring calculations
 * 4. Performing batch operations for effects
 */
object OptimizedStatSystem {
    // LRU cache for fast lookups of commonly accessed values
    private val statValueCache = mutableMapOf<String, Int>()
    private val MAX_CACHE_SIZE = 100

    // Cached ordinal values to avoid repeated reflection
    private val ordinalCache = mutableMapOf<Enum<*>, Int>()

    // Cache for modifier bonuses
    private val modifierBonusCache = mutableMapOf<StatModifier?, Int>()

    /**
     * Calculates total score for a stat set with caching
     */
    fun calculateTotalScore(statTiers: StatTiers): Int {
        val cacheKey = "${statTiers.hashCode()}"

        // Return cached score if available
        statValueCache[cacheKey]?.let {
            return it
        }

        // Calculate score if not cached
        val score = with(statTiers) {
            getEnumScore(tier.value) + getModifierBonus(tier.modifier) +
                    getEnumScore(attackPotency.value) + getModifierBonus(attackPotency.modifier) +
                    getEnumScore(speed.value) + getModifierBonus(speed.modifier) +
                    getEnumScore(liftingStrength.value) + getModifierBonus(liftingStrength.modifier) +
                    getEnumScore(strikingStrength.value) + getModifierBonus(strikingStrength.modifier) +
                    getEnumScore(durability.value) + getModifierBonus(durability.modifier) +
                    getEnumScore(intelligence.value) + getModifierBonus(intelligence.modifier) +
                    getEnumScore(range.value) + getModifierBonus(range.modifier) +
                    getEnumScore(stamina.value) + getModifierBonus(stamina.modifier)
        }

        // Cache the result
        if (statValueCache.size >= MAX_CACHE_SIZE) {
            statValueCache.entries.firstOrNull()?.let {
                statValueCache.remove(it.key)
            }
        }
        statValueCache[cacheKey] = score

        return score
    }

    /**
     * Get cached enum ordinal value with 5x base multiplier and offset
     */
    private fun getEnumScore(value: Enum<*>): Int {
        // Get cached ordinal or compute it
        val ordinal = ordinalCache.getOrPut(value) { value.ordinal }
        return ordinal * StatScoringConfig.BASE_MULTIPLIER + StatScoringConfig.BASE_OFFSET
    }

    /**
     * Get cached modifier bonus value
     */
    private fun getModifierBonus(modifier: StatModifier?): Int {
        return modifierBonusCache.getOrPut(modifier) { modifier?.bonusValue ?: 2 }
    }

    /**
     * Efficiently apply a batch of effects to stats
     */
    suspend fun applyEffectsBatch(
        statTiers: StatTiers,
        effects: List<StatEffect>,
        negationTags: Set<String> = emptySet()
    ): StatTiers = withContext(Dispatchers.Default) {
        // Skip if no effects or all are negated
        if (effects.isEmpty()) return@withContext statTiers

        // Filter only applicable effects once (avoiding repeated filtering)
        val applicableEffects = effects.filter { effect ->
            effect.abilityTag == null || effect.abilityTag !in negationTags
        }

        // Group effects by stat type for more efficient processing
        val effectsByType = applicableEffects
            .filter { it.target == TargetType.SELF }
            .groupBy { it.statType }

        // Clone the stats to avoid mutation issues
        val updatedStats = statTiers.copy(
            tier = statTiers.tier.copy(),
            attackPotency = statTiers.attackPotency.copy(),
            speed = statTiers.speed.copy(),
            liftingStrength = statTiers.liftingStrength.copy(),
            strikingStrength = statTiers.strikingStrength.copy(),
            durability = statTiers.durability.copy(),
            intelligence = statTiers.intelligence.copy(),
            range = statTiers.range.copy(),
            stamina = statTiers.stamina.copy()
        )

        // Apply best effect for each stat type
        effectsByType[StatType.TIER]?.let { applyBestEffect(updatedStats.tier, it) }
        effectsByType[StatType.ATTACK_POTENCY]?.let { applyBestEffect(updatedStats.attackPotency, it) }
        effectsByType[StatType.SPEED]?.let { applyBestEffect(updatedStats.speed, it) }
        effectsByType[StatType.LIFTING_STRENGTH]?.let { applyBestEffect(updatedStats.liftingStrength, it) }
        effectsByType[StatType.STRIKING_STRENGTH]?.let { applyBestEffect(updatedStats.strikingStrength, it) }
        effectsByType[StatType.DURABILITY]?.let { applyBestEffect(updatedStats.durability, it) }
        effectsByType[StatType.INTELLIGENCE]?.let { applyBestEffect(updatedStats.intelligence, it) }
        effectsByType[StatType.RANGE]?.let { applyBestEffect(updatedStats.range, it) }
        effectsByType[StatType.STAMINA]?.let { applyBestEffect(updatedStats.stamina, it) }

        // Invalidate cache for this stat set
        statValueCache.remove("${statTiers.hashCode()}")

        return@withContext updatedStats
    }

    /**
     * Apply the best effect to a stat value
     */
    private fun <T : Enum<T>> applyBestEffect(
        stat: StatValueHolder<T>,
        effects: List<StatEffect>
    ) {
        // Find best effect (with highest change value)
        val bestEffect = effects
            .filterIsInstance<StatEffect.SimpleEffect>()
            .maxByOrNull { it.change } ?: return

        // Get enum constants using reified
        val enumConstants = stat.value::class.java.enumConstants

        // Calculate new ordinal with bounds checking
        val currentOrdinal = stat.value.ordinal
        val newOrdinal = (currentOrdinal + bestEffect.change.toInt())
            .coerceIn(0, enumConstants.size - 1)

        // Apply the effect
        stat.value = enumConstants[newOrdinal]
        stat.modifier = bestEffect.modifier

        // Update cache
        ordinalCache[stat.value] = newOrdinal
    }

    /**
     * Clear caches (call when memory pressure is high)
     */
    fun clearCaches() {
        statValueCache.clear()
        ordinalCache.clear()
        modifierBonusCache.clear()
    }
}