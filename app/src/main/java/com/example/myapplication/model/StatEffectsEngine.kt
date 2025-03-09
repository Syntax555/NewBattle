package com.example.myapplication.model

/**
 * Filters out any StatEffect whose abilityTag is present in the provided negationTags.
 */
fun List<StatEffect>.filterNegated(negationTags: Set<String>): List<StatEffect> {
    return this.filter { effect ->
        effect.abilityTag?.let { it !in negationTags } ?: true
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
    // Filter effects for this stat and target SELF, then remove those negated.
    val applicable = effects.filter { it.statType == statType && it.target == TargetType.SELF }
        .filter { effect -> effect.abilityTag?.let { it !in negationTags } ?: true }
        .mapNotNull { it as? StatEffect.SimpleEffect }
    if (applicable.isEmpty()) return

    val best = applicable.maxByOrNull { it.change } ?: return
    val baseOrdinal = this.value.ordinal
    val enumConstants = enumValues<T>()
    val newOrdinal = (baseOrdinal + best.change.toInt()).coerceIn(0, enumConstants.size - 1)
    this.value = enumConstants[newOrdinal]
    this.modifier = best.modifier
}

/**
 * Applies the best available effects to each stat in StatTiers.
 *
 * Pass a set of negationTags (from enemy resistances, equipment disables, etc.) to filter effects.
 */
fun StatTiers.applyBestEffects(
    effects: List<StatEffect>,
    negationTags: Set<String> = emptySet()
): StatTiers {
    this.attackPotency.applyBestEffects(StatType.ATTACK_POTENCY, effects, negationTags)
    this.speed.applyBestEffects(StatType.SPEED, effects, negationTags)
    this.liftingStrength.applyBestEffects(StatType.LIFTING_STRENGTH, effects, negationTags)
    this.strikingStrength.applyBestEffects(StatType.STRIKING_STRENGTH, effects, negationTags)
    this.durability.applyBestEffects(StatType.DURABILITY, effects, negationTags)
    this.intelligence.applyBestEffects(StatType.INTELLIGENCE, effects, negationTags)
    this.range.applyBestEffects(StatType.RANGE, effects, negationTags)
    this.stamina.applyBestEffects(StatType.STAMINA, effects, negationTags)
    return this
}
