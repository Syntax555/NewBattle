package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.core.model.CharacterKey
import com.example.myapplication.core.model.GameCharacter
import com.example.myapplication.core.model.Origin

/**
 * List of characters with filtering options.
 */
@Composable
fun CharacterList(
    characters: List<GameCharacter>,
    onCharacterSelected: (GameCharacter, CharacterKey) -> Unit,
    selectedCharacter: Pair<GameCharacter, CharacterKey>? = null,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedOrigin by remember { mutableStateOf<Origin?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        // Search and filters
        SearchAndFilterBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            selectedOrigin = selectedOrigin,
            onOriginSelected = { selectedOrigin = it },
            availableOrigins = Origin.values().toList()
        )

        // Filter characters
        val filteredCharacters = characters.filter { character ->
            val matchesSearch = searchQuery.isEmpty() ||
                    character.name.contains(searchQuery, ignoreCase = true) ||
                    character.keys.any { it.name.contains(searchQuery, ignoreCase = true) }

            val matchesOrigin = selectedOrigin == null || character.origin == selectedOrigin

            matchesSearch && matchesOrigin
        }

        // Display filtered characters
        if (filteredCharacters.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No characters found",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                // Group by origin if no specific origin is selected
                if (selectedOrigin == null) {
                    val groupedCharacters = filteredCharacters.groupBy { it.origin }

                    groupedCharacters.forEach { (origin, charactersInOrigin) ->
                        item {
                            Text(
                                text = origin.displayName,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        items(charactersInOrigin) { character ->
                            // For each character, show all its keys
                            character.keys.forEach { key ->
                                val isSelected = selectedCharacter?.let {
                                    it.first.name == character.name && it.second.name == key.name
                                } ?: false

                                CharacterCard(
                                    character = character,
                                    key = key,
                                    isSelected = isSelected,
                                    onClick = { onCharacterSelected(character, key) }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                } else {
                    // If origin is selected, just list the characters
                    items(filteredCharacters) { character ->
                        character.keys.forEach { key ->
                            val isSelected = selectedCharacter?.let {
                                it.first.name == character.name && it.second.name == key.name
                            } ?: false

                            CharacterCard(
                                character = character,
                                key = key,
                                isSelected = isSelected,
                                onClick = { onCharacterSelected(character, key) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Search and filter bar component.
 */
@Composable
fun SearchAndFilterBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedOrigin: Origin?,
    onOriginSelected: (Origin?) -> Unit,
    availableOrigins: List<Origin>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Search field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search Characters") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Origin filter
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Filter by Origin:",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )

            Box(
                modifier = Modifier.weight(1f)
            ) {
                var expanded by remember { mutableStateOf(false) }

                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(selectedOrigin?.displayName ?: "All Origins")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    // All origins option
                    DropdownMenuItem(
                        text = { Text("All Origins") },
                        onClick = {
                            onOriginSelected(null)
                            expanded = false
                        }
                    )

                    // Each available origin
                    availableOrigins.forEach { origin ->
                        DropdownMenuItem(
                            text = { Text(origin.displayName) },
                            onClick = {
                                onOriginSelected(origin)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}