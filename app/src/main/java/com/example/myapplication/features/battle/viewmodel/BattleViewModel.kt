package com.example.battlecompare.features.battle.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.battlecompare.core.model.CharacterKey
import com.example.battlecompare.core.model.GameCharacter
import com.example.battlecompare.core.model.Origin
import com.example.battlecompare.core.model.StatScoringEngine
import com.example.battlecompare.data.repository.CharacterRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for the battle feature.
 * Handles character selection and battle calculations.
 */
class BattleViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow<BattleUiState>(BattleUiState.Loading)
    val uiState: StateFlow<BattleUiState> = _uiState.asStateFlow()

    // Selected characters
    private val _leftSelection = MutableStateFlow<Pair<GameCharacter, CharacterKey>?>(null)
    val leftSelection = _leftSelection.asStateFlow()

    private val _rightSelection = MutableStateFlow<Pair<GameCharacter, CharacterKey>?>(null)
    val rightSelection = _rightSelection.asStateFlow()

    // Filtered lists for selection UI
    private val _availableOrigins = MutableStateFlow<List<Origin>>(emptyList())
    val availableOrigins = _availableOrigins.asStateFlow()

    private val _filteredCharacters = MutableStateFlow<List<GameCharacter>>(emptyList())
    val filteredCharacters = _filteredCharacters.asStateFlow()

    private val _selectedOrigin = MutableStateFlow<Origin?>(null)
    val selectedOrigin = _selectedOrigin.asStateFlow()

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // Battle status
    private val _canStartBattle = MutableStateFlow(false)
    val canStartBattle = _canStartBattle.asStateFlow()

    // Initialize
    init {
        loadCharacters()

        // Update battle availability when selections change
        viewModelScope.launch {
            combine(_leftSelection, _rightSelection) { left, right ->
                left != null && right != null
            }.collect {
                _canStartBattle.value = it
            }
        }
    }

    /**
     * Load all characters from repository
     */
    fun loadCharacters() {
        viewModelScope.launch {
            _uiState.value = BattleUiState.Loading
            try {
                val characters = repository.getAllCharacters()

                _uiState.value = BattleUiState.Success(characters)

                // Extract available origins
                val origins = characters
                    .map { it.origin }
                    .distinct()
                    .sortedBy { it.displayName }

                _availableOrigins.value = origins
                _filteredCharacters.value = characters

            } catch (e: Exception) {
                Timber.e(e, "Failed to load characters")
                _uiState.value = BattleUiState.Error("Failed to load characters: ${e.message}")
            }
        }
    }

    /**
     * Filter characters by origin and search query
     */
    fun filterCharacters(origin: Origin? = null, query: String? = null) {
        viewModelScope.launch {
            val currentState = _uiState.value

            if (currentState is BattleUiState.Success) {
                // Update origin if provided
                if (origin != null) {
                    _selectedOrigin.value = origin
                }

                // Update search query if provided
                if (query != null) {
                    _searchQuery.value = query
                }

                val filterOrigin = _selectedOrigin.value
                val filterQuery = _searchQuery.value

                // Apply filters
                val filtered = currentState.characters.filter { character ->
                    // Origin filter
                    val matchesOrigin = filterOrigin == null || character.origin == filterOrigin

                    // Search query filter
                    val matchesQuery = filterQuery.isEmpty() ||
                            character.name.contains(filterQuery, ignoreCase = true) ||
                            character.keys.any { it.name.contains(filterQuery, ignoreCase = true) }

                    matchesOrigin && matchesQuery
                }

                _filteredCharacters.value = filtered
            }
        }
    }

    /**
     * Select a character for the left side
     */
    fun selectLeftCharacter(character: GameCharacter, key: CharacterKey) {
        _leftSelection.value = character to key
    }

    /**
     * Select a character for the right side
     */
    fun selectRightCharacter(character: GameCharacter, key: CharacterKey) {
        _rightSelection.value = character to key
    }

    /**
     * Clear selections
     */
    fun clearSelections() {
        _leftSelection.value = null
        _rightSelection.value = null
    }

    /**
     * Calculate battle outcome between selected characters
     */
    fun calculateBattleOutcome(): BattleResult? {
        val left = _leftSelection.value ?: return null
        val right = _rightSelection.value ?: return null

        // Use scoring engine to calculate scores
        val leftScore = StatScoringEngine.calculateTotalScore(left.second.statTiers)
        val rightScore = StatScoringEngine.calculateTotalScore(right.second.statTiers)

        return when {
            leftScore > rightScore -> BattleResult.Victory(left, leftScore, right, rightScore)
            rightScore > leftScore -> BattleResult.Victory(right, rightScore, left, leftScore)
            else -> BattleResult.Draw(left, leftScore, right, rightScore)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Clean up any resources
        StatScoringEngine.clearCaches()
    }

    /**
     * Factory for creating the ViewModel with dependencies
     */
    class Factory(private val repository: CharacterRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BattleViewModel::class.java)) {
                return BattleViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

/**
 * UI state for the battle screen
 */
sealed class BattleUiState {
    data object Loading : BattleUiState()
    data class Success(val characters: List<GameCharacter>) : BattleUiState()
    data class Error(val message: String) : BattleUiState()
}

/**
 * Represents the result of a battle
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