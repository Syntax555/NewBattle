package com.example.battlecompare.features.battle.viewmodel

import com.example.battlecompare.core.model.GameCharacter

/**
 * UI state for character-related screens.
 */
sealed class CharactersUiState {
    /**
     * Loading state.
     */
    data object Loading : CharactersUiState()

    /**
     * Success state with loaded characters.
     */
    data class Success(val characters: List<GameCharacter>) : CharactersUiState()

    /**
     * Error state with error message.
     */
    data class Error(val message: String) : CharactersUiState()
}