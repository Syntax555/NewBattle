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
    val baseScore = this.value.ordinal * BASE_MULTIPLIER
    // Default bonus if no modifier is set is 2.
    val bonus = this.modifier?.bonusValue() ?: 2
    return baseScore + bonus + BASE_OFFSET
}

/**
 * Calculates the overall score for a character's StatTiers.
 * You can adjust the weights if necessary.
 */
fun StatTiers.calculateTotalScore(): Int {
    return this.tier.calculateScore() +
            this.attackPotency.calculateScore() +
            this.speed.calculateScore() +
            this.liftingStrength.calculateScore() +
            this.strikingStrength.calculateScore() +
            this.durability.calculateScore() +
            this.intelligence.calculateScore() +
            this.range.calculateScore() +
            this.stamina.calculateScore()
}
