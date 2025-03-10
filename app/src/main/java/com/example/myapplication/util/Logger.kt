// Logger.kt
package com.example.myapplication.util

import android.util.Log
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Enhanced logging utility using Timber.
 */
@Singleton
class Logger @Inject constructor() {
    init {
        // Plant debug tree in debug builds
        if (isDebugBuild()) {
            Timber.plant(Timber.DebugTree())
        } else {
            // Plant production tree for release builds
            Timber.plant(CrashReportingTree())
        }
    }

    /**
     * Log debug message.
     */
    fun d(message: String, vararg args: Any?) {
        Timber.d(message, *args)
    }

    /**
     * Log info message.
     */
    fun i(message: String, vararg args: Any?) {
        Timber.i(message, *args)
    }

    /**
     * Log warning message.
     */
    fun w(message: String, vararg args: Any?) {
        Timber.w(message, *args)
    }

    /**
     * Log error message.
     */
    fun e(message: String, vararg args: Any?) {
        Timber.e(message, *args)
    }

    /**
     * Log error with exception.
     */
    fun e(throwable: Throwable, message: String, vararg args: Any?) {
        Timber.e(throwable, message, *args)
    }

    // Helper for determining if this is a debug build
    private fun isDebugBuild(): Boolean {
        return BuildConfig.DEBUG
    }

    /**
     * Custom Timber tree for production that reports crashes to a hypothetical crash reporting service.
     */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.ERROR || priority == Log.WARN) {
                // In a real app, you would send these to a crash reporting service
                // Example: FirebaseCrashlytics.getInstance().log(message)

                if (t != null) {
                    // Example: FirebaseCrashlytics.getInstance().recordException(t)
                }
            }
        }
    }
}
