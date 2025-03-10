package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.repository.CharacterRepository
import com.example.myapplication.repository.CharacterRepositoryImpl
import kotlinx.serialization.json.Json

/**
 * Simple dependency injection container using the Service Locator pattern.
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

    fun initialize(applicationContext: Context) {
        appContext = applicationContext
    }

    private var characterRepositoryInstance: CharacterRepository? = null

    fun provideCharacterRepository(): CharacterRepository {
        return characterRepositoryInstance ?: synchronized(this) {
            characterRepositoryInstance ?: createCharacterRepository().also {
                characterRepositoryInstance = it
            }
        }
    }

    private fun createCharacterRepository(): CharacterRepository {
        val context = appContext ?: throw IllegalStateException("ServiceLocator not initialized")
        return CharacterRepositoryImpl(context, json)
    }

    // For testing
    fun resetAll() {
        characterRepositoryInstance = null
    }
}