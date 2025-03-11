package com.example.battlecompare.core

import android.content.Context
import androidx.collection.LruCache
import com.example.battlecompare.core.model.GameCharacter
import com.example.battlecompare.core.util.MemoryCache
import com.example.battlecompare.data.repository.CharacterRepository
import com.example.battlecompare.data.repository.CharacterRepositoryImpl
import kotlinx.serialization.json.Json
import timber.log.Timber

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