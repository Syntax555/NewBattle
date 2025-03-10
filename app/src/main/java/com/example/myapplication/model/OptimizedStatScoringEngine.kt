// OptimizedStatScoringEngine.kt
package com.example.myapplication.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Optimized stat calculation engine that leverages coroutines and memoization.
 */
object OptimizedStatScoringEngine {
    // Cache for enum ordinal values to avoid repeated reflection
    private val ordinalCache = mutableMapOf<Enum<*>, Int>()

    // Cache for modifier bonus values
    private val modifierBonusCache = mutableMapOf<StatModifier?, Int>()

    /**
     * Calculates score for a stat value holder with caching of intermediate values.
     */
    inline fun <reified T : Enum<T>> StatValueHolder<T>.calculateScoreOptimized(): Int {
        // Get cached ordinal or compute and cache it
        val ordinal = ordinalCache.getOrPut(this.value) { this.value.ordinal }

        // Get cached modifier bonus or compute and cache it
        val bonus = modifierBonusCache.getOrPut(this.modifier) {
            this.modifier?.bonusValue() ?: 2
        }

        return (ordinal * StatScoringConfig.BASE_MULTIPLIER) +
                bonus +
                StatScoringConfig.BASE_OFFSET
    }

    /**
     * Efficiently calculates total score for a character's stat tiers.
     */
    fun calculateTotalScoreOptimized(statTiers: StatTiers): Int {
        return with(statTiers) {
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

    /**
     * Applies stat effects in batch for better performance.
     * Groups effects by stat type to minimize iteration.
     */
    suspend fun applyEffectsBatch(
        statTiers: StatTiers,
        effects: List<StatEffect>,
        negationTags: Set<String> = emptySet()
    ): StatTiers = withContext(Dispatchers.Default) {
        // Skip if no effects
        if (effects.isEmpty()) return@withContext statTiers

        // Filter negated effects once
        val applicableEffects = if (negationTags.isEmpty()) {
            effects
        } else {
            effects.filterNegated(negationTags)
        }

        // Group effects by stat type for efficient processing
        val effectsByStatType = applicableEffects
            .filter { it.target == TargetType.SELF }
            .groupBy { it.statType }

        // Apply best effect for each stat type
        effectsByStatType[StatType.ATTACK_POTENCY]?.let {
            statTiers.attackPotency.applyBestEffectsOptimized(it)
        }

        effectsByStatType[StatType.SPEED]?.let {
            statTiers.speed.applyBestEffectsOptimized(it)
        }

        effectsByStatType[StatType.LIFTING_STRENGTH]?.let {
            statTiers.liftingStrength.applyBestEffectsOptimized(it)
        }

        effectsByStatType[StatType.STRIKING_STRENGTH]?.let {
            statTiers.strikingStrength.applyBestEffectsOptimized(it)
        }

        effectsByStatType[StatType.DURABILITY]?.let {
            statTiers.durability.applyBestEffectsOptimized(it)
        }

        effectsByStatType[StatType.INTELLIGENCE]?.let {
            statTiers.intelligence.applyBestEffectsOptimized(it)
        }

        effectsByStatType[StatType.RANGE]?.let {
            statTiers.range.applyBestEffectsOptimized(it)
        }

        effectsByStatType[StatType.STAMINA]?.let {
            statTiers.stamina.applyBestEffectsOptimized(it)
        }

        effectsByStatType[StatType.TIER]?.let {
            statTiers.tier.applyBestEffectsOptimized(it)
        }

        statTiers
    }

    /**
     * Optimized version of applyBestEffects for specific stat type.
     */
    inline fun <reified T : Enum<T>> StatValueHolder<T>.applyBestEffectsOptimized(
        effects: List<StatEffect>
    ) {
        // Find the best effect (with highest change value)
        val best = effects.asSequence()
            .filterIsInstance<StatEffect.SimpleEffect>()
            .maxByOrNull { it.change } ?: return

        // Apply the effect
        val enumConstants = enumValues<T>()
        val baseOrdinal = ordinalCache.getOrPut(this.value) { this.value.ordinal }
        val newOrdinal = (baseOrdinal + best.change.toInt()).coerceIn(0, enumConstants.size - 1)

        this.value = enumConstants[newOrdinal]
        this.modifier = best.modifier

        // Update cache with new value
        ordinalCache[this.value] = newOrdinal
    }

    /**
     * Clears the calculation caches.
     * Call this when characters are removed or when memory pressure is high.
     */
    fun clearCaches() {
        ordinalCache.clear()
        modifierBonusCache.clear()
    }
}