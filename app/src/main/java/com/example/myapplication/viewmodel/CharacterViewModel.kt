// CharacterViewModel.kt (simplified)
package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.CharacterKey
import com.example.myapplication.model.GameCharacter
import com.example.myapplication.model.calculateTotalScore
import com.example.myapplication.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for character-related screens.
 */
class CharacterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CharacterRepository(application)

    // UI state
    private val _uiState = MutableStateFlow<CharactersUiState>(CharactersUiState.Loading)
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    // Selected characters
    private val _leftSelection = MutableStateFlow<Pair<GameCharacter, CharacterKey>?>(null)
    val leftSelection = _leftSelection.asStateFlow()

    private val _rightSelection = MutableStateFlow<Pair<GameCharacter, CharacterKey>?>(null)
    val rightSelection = _rightSelection.asStateFlow()

    // Initialize
    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            _uiState.value = CharactersUiState.Loading
            try {
                val characters = repository.loadAllCharacters()
                _uiState.value = CharactersUiState.Success(characters)
            } catch (e: Exception) {
                _uiState.value = CharactersUiState.Error("Failed to load characters: ${e.message}")
            }
        }
    }

    fun selectLeftCharacter(character: GameCharacter, key: CharacterKey) {
        _leftSelection.value = character to key
    }

    fun selectRightCharacter(character: GameCharacter, key: CharacterKey) {
        _rightSelection.value = character to key
    }

    fun calculateBattleOutcome(): BattleResult? {
        val left = _leftSelection.value ?: return null
        val right = _rightSelection.value ?: return null

        val leftScore = left.second.statTiers.calculateTotalScore()
        val rightScore = right.second.statTiers.calculateTotalScore()

        return when {
            leftScore > rightScore -> BattleResult.Victory(left, leftScore, right, rightScore)
            rightScore > leftScore -> BattleResult.Victory(right, rightScore, left, leftScore)
            else -> BattleResult.Draw(left, leftScore, right, rightScore)
        }
    }
}

/**
 * UI state for characters screen.
 */
sealed class CharactersUiState {
    data object Loading : CharactersUiState()
    data class Success(val characters: List<GameCharacter>) : CharactersUiState()
    data class Error(val message: String) : CharactersUiState()
}

/**
 * Battle result.
 */
sealed class BattleResult {
    data class Victory(
        val winner: Pair<GameCharacter, CharacterKey>,
        val winnerScore: Int,
        val loser: Pair<GameCharacter, CharacterKey>,
        val loserScore: Int
    ) : BattleResult()

    data class Draw(
        val left: Pair<GameCharacter, CharacterKey>,
        val leftScore: Int,
        val right: Pair<GameCharacter, CharacterKey>,
        val rightScore: Int
    ) : BattleResult()
}