package com.example.myapplication.repository

import com.example.myapplication.model.CharacterKey
import com.example.myapplication.model.GameCharacter
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharactersFlow(): Flow<List<GameCharacter>>
    suspend fun getAllCharacters(): List<GameCharacter>
    suspend fun getCharacterByName(name: String): GameCharacter?
    suspend fun refreshCharacters()
}