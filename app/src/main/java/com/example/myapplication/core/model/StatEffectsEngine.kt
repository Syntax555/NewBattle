package com.example.myapplication.core.model

/**
 * Filters out any StatEffect whose abilityTag is present in the provided negationTags.
 * Optimized to avoid unnecessary allocation for non-negated effects.
 */
fun List<StatEffect>.filterNegated(negationTags: Set<String>): List<StatEffect> {
    // Skip the operation if negationTags is empty or the list is empty
    if (negationTags.isEmpty() || this.isEmpty()) return this

    return this.filter { effect ->
        // Simplified predicate with null-safe operation
        effect.abilityTag == null || effect.abilityTag !in negationTags
    }
}

/**
 * Generic extension function for any StatValueHolder that applies the best (highest) effect,
 * after filtering out negated effects.
 *
 * @param statType The StatType corresponding to this stat.
 * @param effects The list of stat effects to consider.
 * @param negationTags A set of tags representing resistances/disables.
 */
inline fun <reified T : Enum<T>> StatValueHolder<T>.applyBestEffects(
    statType: StatType,
    effects: List<StatEffect>,
    negationTags: Set<String> = emptySet()
) {
    // Early return if effects list is empty
    if (effects.isEmpty()) return

    // Single-pass filtering combining all predicates
    val best = effects.asSequence()
        .filter { it.statType == statType && it.target == TargetType.SELF }
        .filter { it.abilityTag == null || it.abilityTag !in negationTags }
        .filterIsInstance<StatEffect.SimpleEffect>()
        .maxByOrNull { it.change } ?: return

    // Apply the effect
    val baseOrdinal = this.value.ordinal
    val enumConstants = enumValues<T>()
    val newOrdinal = (baseOrdinal + best.change.toInt()).coerceIn(0, enumConstants.size - 1)
    this.value = enumConstants[newOrdinal]
    this.modifier = best.modifier
}

/**
 * Applies the best available effects to each stat in StatTiers.
 *
 * Optimized to filter applicable effects once for all stats.
 *
 * @param effects The list of stat effects to consider.
 * @param negationTags A set of tags representing resistances/disables.
 * @return The modified StatTiers instance for method chaining.
 */
fun StatTiers.applyBestEffects(
    effects: List<StatEffect>,
    negationTags: Set<String> = emptySet()
): StatTiers {
    // Skip processing if effects list is empty
    if (effects.isEmpty()) return this

    // Filter effects once to avoid redundant filtering for each stat
    val applicableEffects = if (negationTags.isEmpty()) {
        effects
    } else {
        effects.filterNegated(negationTags)
    }

    // Apply effects to each stat
    attackPotency.applyBestEffects(StatType.ATTACK_POTENCY, applicableEffects)
    speed.applyBestEffects(StatType.SPEED, applicableEffects)
    liftingStrength.applyBestEffects(StatType.LIFTING_STRENGTH, applicableEffects)
    strikingStrength.applyBestEffects(StatType.STRIKING_STRENGTH, applicableEffects)
    durability.applyBestEffects(StatType.DURABILITY, applicableEffects)
    intelligence.applyBestEffects(StatType.INTELLIGENCE, applicableEffects)
    range.applyBestEffects(StatType.RANGE, applicableEffects)
    stamina.applyBestEffects(StatType.STAMINA, applicableEffects)

    return this
}