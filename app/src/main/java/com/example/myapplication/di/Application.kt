// Application.kt
package com.example.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom Application class for Hilt dependency injection.
 */
@HiltAndroidApp
class BattleCompareApplication : Application()