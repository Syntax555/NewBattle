package com.example.myapplication.core.model

/**
 * Configuration parameters for stat scoring.
 * Adjust these values to change the overall spacing and baseline.
 */
object StatScoringConfig {
    // Each base stat level is spaced BASE_MULTIPLIER points apart.
    const val BASE_MULTIPLIER: Int = 5
    // Adding a baseline offset ensures the lowest stat starts above 0.
    const val BASE_OFFSET: Int = 1
}
