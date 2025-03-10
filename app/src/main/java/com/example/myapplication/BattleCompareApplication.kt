package com.example.myapplication

import android.app.Application
import com.example.myapplication.di.ServiceLocator
import com.example.myapplication.model.OptimizedStatSystem
import timber.log.Timber

class BattleCompareApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize logging for debug builds only
        if (isDebugBuild()) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Debug logging initialized")
        }

        // Initialize the service locator
        ServiceLocator.initialize(applicationContext)

        // Preload character data in background
        preloadData()
    }

    /**
     * Helper method to preload essential data in background
     */
    private fun preloadData() {
        // This would be a good place to use WorkManager for
        // more complex preloading scenarios
        Thread {
            try {
                // Trigger initial character loading
                ServiceLocator.provideCharacterRepository()
                Timber.d("Preloading character data complete")
            } catch (e: Exception) {
                Timber.e(e, "Error preloading character data")
            }
        }.start()
    }

    /**
     * Helper method to determine if this is a debug build
     */
    private fun isDebugBuild(): Boolean {
        return applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    override fun onLowMemory() {
        super.onLowMemory()
        // Clear caches when memory is low
        OptimizedStatSystem.clearCaches()
        Timber.d("Cleared caches due to low memory")
    }
}