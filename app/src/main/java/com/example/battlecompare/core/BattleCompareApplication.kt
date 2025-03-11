package com.example.battlecompare.core

import android.app.Application
import android.content.Context
import androidx.collection.LruCache
import com.example.battlecompare.core.model.GameCharacter
import com.example.battlecompare.core.model.StatScoringEngine
import com.example.battlecompare.core.util.MemoryCache
import com.example.battlecompare.data.repository.CharacterRepository
import com.example.battlecompare.data.repository.CharacterRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import timber.log.Timber

/**
 * Main application class that initializes core components.
 */
class BattleCompareApplication : Application() {

    // Application-level coroutine scope
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        // Initialize logging
        if (isDebugBuild()) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Debug logging initialized")
        }

        // Initialize service locator
        AppDependencies.initialize(applicationContext)

        // Preload data in background
        preloadData()
    }

    /**
     * Preload essential data in background
     */
    private fun preloadData() {
        applicationScope.launch {
            try {
                // Trigger initial character loading
                AppDependencies.provideCharacterRepository().getAllCharacters()
                Timber.d("Preloading character data complete")
            } catch (e: Exception) {
                Timber.e(e, "Error preloading character data")
            }
        }
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
        StatScoringEngine.clearCaches()
        Timber.d("Cleared caches due to low memory")
    }
}

/**
 * Centralized dependency provider.
 * This is a simplified dependency injection solution.
 */
object AppDependencies {

    private lateinit var appContext: Context

    // JSON parser with optimized settings
    private val json by lazy {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        }
    }

    // Cache for dependencies
    private val componentCache = LruCache<String, Any>(10)

    // Character cache
    private val characterCache by lazy {
        MemoryCache<String, GameCharacter>()
    }

    /**
     * Initialize dependencies with application context
     */
    fun initialize(context: Context) {
        appContext = context.applicationContext
        Timber.d("AppDependencies initialized")
    }

    /**
     * Provide character repository
     */
    fun provideCharacterRepository(): CharacterRepository {
        val key = "character_repository"

        return componentCache.get(key) as? CharacterRepository ?: synchronized(this) {
            val repo = CharacterRepositoryImpl(appContext, json, characterCache)
            componentCache.put(key, repo)
            repo
        }
    }

    /**
     * Provide JSON parser
     */
    fun provideJson(): Json = json

    /**
     * Reset all dependencies (useful for testing)
     */
    fun resetAll() {
        componentCache.evictAll()
        characterCache.clear()
        Timber.d("All dependencies reset")
    }
}

/**
 * Memory cache implementation with improved lifecycle management
 */
class MemoryCache<K : Any, V : Any> {
    private val cache = LruCache<K, V>(calculateCacheSize())

    /**
     * Get a value from the cache
     */
    fun get(key: K): V? = cache.get(key)

    /**
     * Put a value in the cache
     */
    fun put(key: K, value: V) = cache.put(key, value)

    /**
     * Remove a value from the cache
     */
    fun remove(key: K) = cache.remove(key)

    /**
     * Clear all values from the cache
     */
    fun clear() = cache.evictAll()

    /**
     * Calculate optimal cache size based on available memory
     */
    private fun calculateCacheSize(): Int {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        // Use 1/8th of available memory
        return maxMemory / 8
    }
}