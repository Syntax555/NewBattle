package com.example.myapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.CharacterKey
import com.example.myapplication.model.GameCharacter
import com.example.myapplication.model.Origin

// Represents the different steps for selection in a panel.
enum class SelectionStep {
    Origin,
    Character,
    Key,
    Confirmed
}

@Composable
fun GameScreen(
    gameCharacters: List<GameCharacter>,
    onBackToMenu: () -> Unit,
    onBattleInitiated: (
        leftSelection: Pair<GameCharacter, CharacterKey>,
        rightSelection: Pair<GameCharacter, CharacterKey>
    ) -> Unit
) {
    var leftSelection by remember { mutableStateOf<Pair<GameCharacter, CharacterKey>?>(null) }
    var rightSelection by remember { mutableStateOf<Pair<GameCharacter, CharacterKey>?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top row with back button
        Button(
            onClick = {
                leftSelection = null
                rightSelection = null
                onBackToMenu()
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Back to Main Menu")
        }

        // Selection panels
        Row(modifier = Modifier.weight(1f)) {
            SelectionPanel(
                gameCharacters = gameCharacters,
                panelTitle = "Left Panel",
                onFinalSelection = { leftSelection = it },
                defaultSelection = leftSelection,
                modifier = Modifier.weight(1f)
            )
            SelectionPanel(
                gameCharacters = gameCharacters,
                panelTitle = "Right Panel",
                onFinalSelection = { rightSelection = it },
                defaultSelection = rightSelection,
                modifier = Modifier.weight(1f)
            )
        }

        // Battle button
        if (leftSelection != null && rightSelection != null) {
            Button(
                onClick = { onBattleInitiated(leftSelection!!, rightSelection!!) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Battle!")
            }
        }
    }
}

@Composable
fun SelectionPanel(
    gameCharacters: List<GameCharacter>,
    panelTitle: String,
    onFinalSelection: (Pair<GameCharacter, CharacterKey>) -> Unit,
    defaultSelection: Pair<GameCharacter, CharacterKey>?,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableStateOf(
        if (defaultSelection != null) SelectionStep.Confirmed else SelectionStep.Origin
    ) }
    var selectedOrigin by remember { mutableStateOf<Origin?>(defaultSelection?.first?.origin) }
    var selectedCharacter by remember { mutableStateOf<GameCharacter?>(defaultSelection?.first) }
    var selectedKey by remember { mutableStateOf<CharacterKey?>(defaultSelection?.second) }

    Column(modifier = modifier.padding(8.dp)) {
        Text(text = panelTitle, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        when (currentStep) {
            SelectionStep.Origin -> {
                Text(text = "Select Origin", style = MaterialTheme.typography.titleMedium)

                // Filter origins with at least 2 characters
                val validOrigins = remember(gameCharacters) {
                    gameCharacters.groupBy { it.origin }
                        .filter { it.value.size >= 2 }
                        .keys
                        .toList()
                }

                LazyColumn {
                    items(validOrigins) { origin ->
                        SelectionCard(text = origin.displayName) {
                            selectedOrigin = origin
                            selectedCharacter = null
                            selectedKey = null
                            currentStep = SelectionStep.Character
                        }
                    }
                }
            }

            SelectionStep.Character -> {
                BackButton(text = "Back to Origins") {
                    currentStep = SelectionStep.Origin
                    selectedOrigin = null
                }

                Text(text = "Select Character", style = MaterialTheme.typography.titleMedium)

                // Filter characters by selected origin
                val filteredCharacters = remember(selectedOrigin, gameCharacters) {
                    gameCharacters.filter { it.origin == selectedOrigin }
                }

                LazyColumn {
                    items(filteredCharacters) { character ->
                        SelectionCard(text = character.name) {
                            selectedCharacter = character

                            // Auto-select key if only one exists
                            if (character.keys.size == 1) {
                                selectedKey = character.keys.first()
                                currentStep = SelectionStep.Confirmed
                            } else {
                                currentStep = SelectionStep.Key
                            }
                        }
                    }
                }
            }

            SelectionStep.Key -> {
                BackButton(text = "Back to Characters") {
                    currentStep = SelectionStep.Character
                    selectedCharacter = null
                }

                selectedCharacter?.let { character ->
                    Text(text = "Select Character Key", style = MaterialTheme.typography.titleMedium)

                    LazyColumn {
                        items(character.keys) { key ->
                            SelectionCard(text = key.name) {
                                selectedKey = key
                                currentStep = SelectionStep.Confirmed
                            }
                        }
                    }
                }
            }

            SelectionStep.Confirmed -> {
                Button(
                    onClick = {
                        selectedCharacter?.let { character ->
                            selectedKey?.let { key ->
                                onFinalSelection(character to key)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirm Selection")
                }

                BackButton(text = "Back to Keys") {
                    currentStep = SelectionStep.Key
                }

                // Show selection summary
                selectedCharacter?.let { character ->
                    selectedKey?.let { key ->
                        Card(modifier = Modifier.padding(top = 8.dp)) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Selected: ${character.name}")
                                Text("Key: ${key.name}")
                                Text("Origin: ${character.origin.displayName}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectionCard(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(onClick = onClick)
    ) {
        Text(text = text, modifier = Modifier.padding(8.dp))
    }
}

@Composable
private fun BackButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(text)
    }
}