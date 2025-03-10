package com.example.myapplication.di

import android.content.Context
import androidx.collection.LruCache
import com.example.myapplication.repository.CharacterRepository
import com.example.myapplication.repository.CharacterRepositoryImpl
import com.example.myapplication.util.MemoryCache
import kotlinx.serialization.json.Json
import timber.log.Timber

/**
 * Simple dependency injection container using the Service Locator pattern.
 * This provides a centralized place to manage dependencies without
 * a full DI framework like Hilt or Koin.
 */
object ServiceLocator {

    private val json by lazy {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = false
            coerceInputValues = true
        }
    }

    private var appContext: Context? = null

    // Cache for dependencies to avoid recreating them
    private val componentCache = LruCache<String, Any>(10)

    /**
     * Initialize the service locator with application context
     */
    fun initialize(applicationContext: Context) {
        appContext = applicationContext.applicationContext
        Timber.d("ServiceLocator initialized")
    }

    /**
     * Provides the character repository, creating it if necessary
     */
    fun provideCharacterRepository(): CharacterRepository {
        val key = "character_repository"

        return componentCache.get(key) as? CharacterRepository ?: synchronized(this) {
            val repo = createCharacterRepository()
            componentCache.put(key, repo)
            repo
        }
    }

    /**
     * Create a new character repository instance
     */
    private fun createCharacterRepository(): CharacterRepository {
        val context = appContext ?: throw IllegalStateException("ServiceLocator not initialized")
        return CharacterRepositoryImpl(context, json)
    }

    /**
     * Provides a generic memory cache
     * Note: We need non-nullable types for the cache
     */
    @Suppress("UNCHECKED_CAST")
    fun <K : Any, V : Any> provideMemoryCache(): MemoryCache<K, V> {
        val key = "memory_cache"

        return componentCache.get(key) as? MemoryCache<K, V> ?: synchronized(this) {
            val cache = MemoryCache<K, V>()
            componentCache.put(key, cache)
            cache
        }
    }

    /**
     * Reset all dependencies (useful for testing)
     */
    fun resetAll() {
        componentCache.evictAll()
        Timber.d("All service locator dependencies reset")
    }
}