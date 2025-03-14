package com.example.battlecompare.core.util

import androidx.collection.LruCache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Generic memory cache that implements the LRU (Least Recently Used) algorithm.
 * Thread-safe with coroutine support.
 */
class MemoryCache<K : Any, V : Any> {
    private val mutex = Mutex()
    private val cache: LruCache<K, V>

    init {
        // Determine cache size based on available memory
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        // Use 1/8th of the available memory for this cache
        val cacheSize = maxMemory / 8

        cache = object : LruCache<K, V>(cacheSize) {
            override fun sizeOf(key: K, value: V): Int {
                // Default implementation - override for custom sizing
                return 1
            }
        }
    }

    /**
     * Get a value from the cache.
     */
    suspend fun get(key: K): V? = mutex.withLock {
        return cache.get(key)
    }

    /**
     * Synchronous get operation for cases where coroutines aren't available
     */
    fun getSync(key: K): V? {
        return cache.get(key)
    }

    /**
     * Put a value in the cache.
     */
    suspend fun put(key: K, value: V) = mutex.withLock {
        cache.put(key, value)
    }

    /**
     * Synchronous put operation for cases where coroutines aren't available
     */
    fun putSync(key: K, value: V) {
        cache.put(key, value)
    }

    /**
     * Remove a value from the cache.
     */
    suspend fun remove(key: K) = mutex.withLock {
        cache.remove(key)
    }

    /**
     * Clear all values from the cache.
     */
    suspend fun clear() = mutex.withLock {
        cache.evictAll()
    }

    /**
     * Synchronous clear operation
     */
    fun clearSync() {
        cache.evictAll()
    }

    /**
     * Get or compute a value if not in cache.
     */
    suspend fun getOrPut(key: K, defaultValue: suspend () -> V): V = mutex.withLock {
        val value = cache.get(key)
        if (value == null) {
            val newValue = defaultValue()
            cache.put(key, newValue)
            newValue
        } else {
            value
        }
    }
}