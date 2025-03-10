package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.di.ServiceLocator
import com.example.myapplication.model.CharacterKey
import com.example.myapplication.model.GameCharacter
import com.example.myapplication.model.OptimizedStatScoringEngine
import com.example.myapplication.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class CharacterViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

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
                val characters = repository.getAllCharacters()
                _uiState.value = CharactersUiState.Success(characters)
            } catch (e: Exception) {
                Timber.e(e, "Failed to load characters")
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

        // Use the optimized scoring engine for better performance
        val leftScore = OptimizedStatScoringEngine.calculateTotalScoreOptimized(left.second.statTiers)
        val rightScore = OptimizedStatScoringEngine.calculateTotalScoreOptimized(right.second.statTiers)

        return when {
            leftScore > rightScore -> BattleResult.Victory(left, leftScore, right, rightScore)
            rightScore > leftScore -> BattleResult.Victory(right, rightScore, left, leftScore)
            else -> BattleResult.Draw(left, leftScore, right, rightScore)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Clean up any resources
        OptimizedStatScoringEngine.clearCaches()
    }

    // Factory to create ViewModel with the repository
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CharacterViewModel::class.java)) {
                return CharacterViewModel(ServiceLocator.provideCharacterRepository()) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

sealed class CharactersUiState {
    data object Loading : CharactersUiState()
    data class Success(val characters: List<GameCharacter>) : CharactersUiState()
    data class Error(val message: String) : CharactersUiState()
}

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