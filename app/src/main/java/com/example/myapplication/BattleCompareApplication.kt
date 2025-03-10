package com.example.myapplication

import android.app.Application
import com.example.myapplication.di.ServiceLocator
import timber.log.Timber

class BattleCompareApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Change this line to use a simpler debug check
        if (isDebugBuild()) {
            Timber.plant(Timber.DebugTree())
        }

        // Initialize the service locator
        ServiceLocator.initialize(applicationContext)
    }

    // Helper method to determine if this is a debug build
    private fun isDebugBuild(): Boolean {
        return applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE != 0
    }
}