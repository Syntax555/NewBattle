package com.example.myapplication.model

import com.example.myapplication.model.StatScoringConfig.BASE_MULTIPLIER
import com.example.myapplication.model.StatScoringConfig.BASE_OFFSET

/**
 * Extension function for any StatValueHolder to calculate its effective score.
 *
 * The effective score is calculated as:
 *    effectiveScore = (ordinal * BASE_MULTIPLIER) + modifierBonus + BASE_OFFSET
 *
 * This ensures each stat level is spaced by BASE_MULTIPLIER points and even the lowest level is above 0.
 */
inline fun <reified T : Enum<T>> StatValueHolder<T>.calculateScore(): Int {
    val baseScore = value.ordinal * BASE_MULTIPLIER
    // Using direct property access instead of method call with the optimized StatModifier
    val bonus = modifier?.bonusValue ?: 2
    return baseScore + bonus + BASE_OFFSET
}

/**
 * Calculates the overall score for a character's StatTiers.
 * You can adjust the weights if necessary.
 */
fun StatTiers.calculateTotalScore(): Int = with(this) {
    tier.calculateScore() +
            attackPotency.calculateScore() +
            speed.calculateScore() +
            liftingStrength.calculateScore() +
            strikingStrength.calculateScore() +
            durability.calculateScore() +
            intelligence.calculateScore() +
            range.calculateScore() +
            stamina.calculateScore()
}