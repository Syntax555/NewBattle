// PerformanceMonitor.kt
package com.example.myapplication.util

import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

/**
 * Utility for monitoring and reporting performance metrics.
 */
@Singleton
class PerformanceMonitor @Inject constructor(
    private val analytics: Analytics
) {
    // Keep track of performance thresholds
    private val slowOperationThresholdMs = 100
    private val verySlowOperationThresholdMs = 500

    /**
     * Track the execution time of a suspend function.
     */
    suspend fun <T> trackOperation(operationName: String, operation: suspend () -> T): T {
        val startTime = System.currentTimeMillis()
        try {
            return operation()
        } finally {
            val duration = System.currentTimeMillis() - startTime
            trackOperationTime(operationName, duration)
        }
    }

    /**
     * Track the execution time of a function.
     */
    fun <T> trackSyncOperation(operationName: String, operation: () -> T): T {
        val startTime = System.currentTimeMillis()
        try {
            return operation()
        } finally {
            val duration = System.currentTimeMillis() - startTime
            trackOperationTime(operationName, duration)
        }
    }

    /**
     * Record operation time and report if slow.
     */
    private fun trackOperationTime(operationName: String, durationMs: Long) {
        // Always log the metric
        analytics.trackPerformanceMetric(operationName, durationMs)

        // Log warnings for slow operations
        when {
            durationMs >= verySlowOperationThresholdMs -> {
                // Very slow operation
                analytics.trackError(
                    "PerformanceIssue",
                    "Very slow operation: $operationName took $durationMs ms"
                )
            }
            durationMs >= slowOperationThresholdMs -> {
                // Slow operation
                analytics.trackError(
                    "PerformanceWarning",
                    "Slow operation: $operationName took $durationMs ms"
                )
            }
        }
    }

    /**
     * Execute a monitored operation with a timeout.
     * If the operation takes longer than the timeout, it will be cancelled and an error reported.
     */
    suspend fun <T> executeWithTimeout(
        operationName: String,
        timeoutMs: Long,
        operation: suspend () -> T
    ): Result<T> = withContext(Dispatchers.Default) {
        try {
            val result = withTimeout(timeoutMs) {
                trackOperation(operationName, operation)
            }
            Result.success(result)
        } catch (e: TimeoutCancellationException) {
            analytics.trackError(
                "OperationTimeout",
                "$operationName timed out after ${timeoutMs}ms",
                e.stackTraceToString()
            )
            Result.failure(e)
        } catch (e: Exception) {
            analytics.trackError(
                "OperationError",
                "$operationName failed: ${e.message}",
                e.stackTraceToString()
            )
            Result.failure(e)
        }
    }
}