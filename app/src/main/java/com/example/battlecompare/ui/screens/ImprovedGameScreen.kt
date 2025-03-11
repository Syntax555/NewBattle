package com.example.battlecompare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.battlecompare.core.model.CharacterKey
import com.example.battlecompare.core.model.GameCharacter
import com.example.battlecompare.core.model.Origin
import com.example.battlecompare.ui.components.CharacterCard
import com.example.battlecompare.ui.components.LoadingIndicator
import com.example.battlecompare.ui.components.ErrorDisplay
import com.example.battlecompare.viewmodel.CharactersUiState
// Add this import to the top of your file
import androidx.compose.foundation.shape.CircleShape

// Selection steps for each panel
enum class SelectionStep {
    ORIGIN, // Select character origin (Marvel, DC, etc.)
    CHARACTER, // Select the character (e.g., Hulk, Superman)
    KEY, // Select the specific version/key of the character
    CONFIRMED // Selection confirmed and ready for battle
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImprovedGameScreen(
    uiState: CharactersUiState,
    leftSelection: Pair<GameCharacter, CharacterKey>?,
    rightSelection: Pair<GameCharacter, CharacterKey>?,
    onSelectLeftCharacter: (GameCharacter, CharacterKey) -> Unit,
    onSelectRightCharacter: (GameCharacter, CharacterKey) -> Unit,
    onBackToMenu: () -> Unit,
    onBattleInitiated: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character Selection") },
                navigationIcon = {
                    IconButton(onClick = onBackToMenu) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is CharactersUiState.Loading -> {
                    LoadingIndicator(message = "Loading characters...")
                }
                is CharactersUiState.Error -> {
                    ErrorDisplay(
                        message = uiState.message,
                        onRetry = { /* Add retry functionality */ }
                    )
                }
                is CharactersUiState.Success -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Battle status header
                        BattleHeader(
                            leftSelection = leftSelection,
                            rightSelection = rightSelection,
                            onBattleInitiated = onBattleInitiated
                        )

                        // Main selection area
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            // Left selection panel
                            SelectionPanel(
                                panelTitle = "Left Fighter",
                                allCharacters = uiState.characters,
                                currentSelection = leftSelection,
                                onSelectionConfirmed = onSelectLeftCharacter,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                            )

                            // Divider
                            Divider(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                                    .padding(vertical = 8.dp)
                            )

                            // Right selection panel
                            SelectionPanel(
                                panelTitle = "Right Fighter",
                                allCharacters = uiState.characters,
                                currentSelection = rightSelection,
                                onSelectionConfirmed = onSelectRightCharacter,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BattleHeader(
    leftSelection: Pair<GameCharacter, CharacterKey>?,
    rightSelection: Pair<GameCharacter, CharacterKey>?,
    onBattleInitiated: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left selection summary
            SelectionSummary(
                selection = leftSelection,
                alignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            )

            // VS and Battle button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "VS",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onBattleInitiated,
                    enabled = leftSelection != null && rightSelection != null,
                    modifier = Modifier.width(120.dp)
                ) {
                    Text("BATTLE!")
                }
            }

            // Right selection summary
            SelectionSummary(
                selection = rightSelection,
                alignment = Alignment.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SelectionSummary(
    selection: Pair<GameCharacter, CharacterKey>?,
    alignment: Alignment.Horizontal,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = when(alignment) {
            Alignment.Start -> Alignment.CenterStart
            Alignment.End -> Alignment.CenterEnd
            else -> Alignment.Center
        }
    ) {
        if (selection != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (alignment == Alignment.Start) Arrangement.Start else Arrangement.End
            ) {
                if (alignment == Alignment.End) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = selection.first.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = selection.second.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Character image
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(selection.second.imagePath)
                            .crossfade(true)
                            .build(),
                        contentDescription = "${selection.first.name} (${selection.second.name})",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                } else {
                    // Character image
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(selection.second.imagePath)
                            .crossfade(true)
                            .build(),
                        contentDescription = "${selection.first.name} (${selection.second.name})",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = selection.first.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = selection.second.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        } else {
            Text(
                text = "Not Selected",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SelectionPanel(
    panelTitle: String,
    allCharacters: List<GameCharacter>,
    currentSelection: Pair<GameCharacter, CharacterKey>?,
    onSelectionConfirmed: (GameCharacter, CharacterKey) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableStateOf(
        if (currentSelection != null) SelectionStep.CONFIRMED else SelectionStep.ORIGIN
    ) }

    var selectedOrigin by remember { mutableStateOf<Origin?>(currentSelection?.first?.origin) }
    var selectedCharacter by remember { mutableStateOf<GameCharacter?>(currentSelection?.first) }
    var selectedKey by remember { mutableStateOf<CharacterKey?>(currentSelection?.second) }

    Column(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        // Panel header
        Text(
            text = panelTitle,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Selection steps indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StepIndicator(
                text = "Origin",
                isActive = currentStep == SelectionStep.ORIGIN,
                isCompleted = currentStep.ordinal > SelectionStep.ORIGIN.ordinal,
                onClick = {
                    if (currentStep != SelectionStep.ORIGIN) {
                        currentStep = SelectionStep.ORIGIN
                        selectedCharacter = null
                        selectedKey = null
                    }
                }
            )

            StepIndicator(
                text = "Character",
                isActive = currentStep == SelectionStep.CHARACTER,
                isCompleted = currentStep.ordinal > SelectionStep.CHARACTER.ordinal,
                isEnabled = selectedOrigin != null,
                onClick = {
                    if (currentStep != SelectionStep.CHARACTER && selectedOrigin != null) {
                        currentStep = SelectionStep.CHARACTER
                        selectedKey = null
                    }
                }
            )

            StepIndicator(
                text = "Variant",
                isActive = currentStep == SelectionStep.KEY,
                isCompleted = currentStep.ordinal > SelectionStep.KEY.ordinal,
                isEnabled = selectedCharacter != null,
                onClick = {
                    if (currentStep != SelectionStep.KEY && selectedCharacter != null) {
                        currentStep = SelectionStep.KEY
                    }
                }
            )
        }

        // Selection content based on current step
        when (currentStep) {
            SelectionStep.ORIGIN -> {
                OriginSelectionList(
                    allCharacters = allCharacters,
                    selectedOrigin = selectedOrigin,
                    onOriginSelected = { origin ->
                        selectedOrigin = origin
                        currentStep = SelectionStep.CHARACTER
                    }
                )
            }

            SelectionStep.CHARACTER -> {
                CharacterSelectionList(
                    characters = allCharacters.filter { it.origin == selectedOrigin },
                    selectedCharacter = selectedCharacter,
                    onCharacterSelected = { character ->
                        selectedCharacter = character

                        // If character has only one key, auto-select it
                        if (character.keys.size == 1) {
                            selectedKey = character.keys.first()
                            onSelectionConfirmed(character, character.keys.first())
                            currentStep = SelectionStep.CONFIRMED
                        } else {
                            currentStep = SelectionStep.KEY
                        }
                    }
                )
            }

            SelectionStep.KEY -> {
                KeySelectionList(
                    character = selectedCharacter,
                    selectedKey = selectedKey,
                    onKeySelected = { key ->
                        selectedKey = key
                        selectedCharacter?.let { character ->
                            onSelectionConfirmed(character, key)
                            currentStep = SelectionStep.CONFIRMED
                        }
                    }
                )
            }

            SelectionStep.CONFIRMED -> {
                // Show the full character details
                selectedCharacter?.let { character ->
                    selectedKey?.let { key ->
                        CharacterCard(
                            character = character,
                            key = key,
                            isSelected = true
                        )

                        Button(
                            onClick = {
                                currentStep = SelectionStep.ORIGIN
                                selectedOrigin = null
                                selectedCharacter = null
                                selectedKey = null
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Text("Change Selection")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StepIndicator(
    text: String,
    isActive: Boolean,
    isCompleted: Boolean,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            enabled = isEnabled,
            onClick = onClick
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.dp)
                .background(
                    when {
                        isActive -> MaterialTheme.colorScheme.primary
                        isCompleted -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    CircleShape
                )
        ) {
            if (isCompleted) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            } else {
                Text(
                    text = (if (isActive) "●" else "○"),
                    color = if (isActive)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = when {
                isActive -> MaterialTheme.colorScheme.primary
                isCompleted -> MaterialTheme.colorScheme.secondary
                !isEnabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Composable
fun OriginSelectionList(
    allCharacters: List<GameCharacter>,
    selectedOrigin: Origin?,
    onOriginSelected: (Origin) -> Unit
) {
    Text(
        text = "Select Origin",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    // Get available origins and count characters for each
    val origins = allCharacters
        .groupBy { it.origin }
        .mapValues { it.value.size }
        .toList()
        .sortedByDescending { it.second } // Sort by character count

    LazyColumn {
        items(origins) { (origin, count) ->
            OriginSelectionItem(
                origin = origin,
                characterCount = count,
                isSelected = origin == selectedOrigin,
                onClick = { onOriginSelected(origin) }
            )
        }
    }
}

@Composable
fun OriginSelectionItem(
    origin: Origin,
    characterCount: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = origin.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$characterCount characters",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun CharacterSelectionList(
    characters: List<GameCharacter>,
    selectedCharacter: GameCharacter?,
    onCharacterSelected: (GameCharacter) -> Unit
) {
    Text(
        text = "Select Character",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    if (characters.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No characters available",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn {
            items(characters) { character ->
                CharacterSelectionItem(
                    character = character,
                    isSelected = character == selectedCharacter,
                    onClick = { onCharacterSelected(character) }
                )
            }
        }
    }
}

@Composable
fun CharacterSelectionItem(
    character: GameCharacter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Use primary key image as thumbnail
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.keys.firstOrNull()?.imagePath)
                    .crossfade(true)
                    .build(),
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${character.keys.size} variants",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun KeySelectionList(
    character: GameCharacter?,
    selectedKey: CharacterKey?,
    onKeySelected: (CharacterKey) -> Unit
) {
    if (character == null) {
        return
    }

    Text(
        text = "Select Variant",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Text(
        text = character.name,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    LazyColumn {
        items(character.keys) { key ->
            KeySelectionItem(
                key = key,
                isSelected = key == selectedKey,
                onClick = { onKeySelected(key) }
            )
        }
    }
}

@Composable
fun KeySelectionItem(
    key: CharacterKey,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Key image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(key.imagePath)
                    .crossfade(true)
                    .build(),
                contentDescription = key.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = key.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // Show some classifications
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    key.classifications.take(2).forEach { classification ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Text(
                                text = classification.label,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    if (key.classifications.size > 2) {
                        Text(
                            text = "+${key.classifications.size - 2} more",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}