// AppModule.kt
package com.example.myapplication.di

import com.example.myapplication.repository.CharacterRepositoryImpl
import com.example.myapplication.repository.CharacterRepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for application-level dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * Binds the CharacterRepositoryImpl implementation to the CharacterRepositoryInterface.
     */
    @Binds
    @Singleton
    abstract fun bindCharacterRepository(
        impl: CharacterRepositoryImpl
    ): CharacterRepositoryInterface
}