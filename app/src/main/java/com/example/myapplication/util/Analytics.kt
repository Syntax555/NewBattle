// Analytics.kt
package com.example.myapplication.util

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Analytics utility for tracking user actions and app events.
 */
@Singleton
class Analytics @Inject constructor(
    private val logger: Logger
) {
    /**
     * Track a screen view.
     */
    fun trackScreenView(screenName: String) {
        // In a real app, you would send this to an analytics service
        // Example: FirebaseAnalytics.getInstance().logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params)
        logger.d("Screen view: $screenName")
    }

    /**
     * Track a character selection.
     */
    fun trackCharacterSelection(characterName: String, keyName: String) {
        logger.d("Character selected: $characterName ($keyName)")
        // Add analytics service call here
    }

    /**
     * Track a battle result.
     */
    fun trackBattleResult(leftName: String, rightName: String, winnerName: String) {
        logger.d("Battle result: $leftName vs $rightName, winner: $winnerName")
        // Add analytics service call here
    }

    /**
     * Track application performance metrics.
     */
    fun trackPerformanceMetric(metricName: String, durationMs: Long) {
        logger.d("Performance: $metricName took $durationMs ms")
        // Add analytics service call here
    }

    /**
     * Track application error.
     */
    fun trackError(errorName: String, errorMessage: String, stackTrace: String? = null) {
        logger.e("Error: $errorName - $errorMessage")
        // Add analytics service call here
    }
}