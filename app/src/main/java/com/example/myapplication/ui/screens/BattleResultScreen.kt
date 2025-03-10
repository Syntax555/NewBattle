package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.viewmodel.BattleResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleResultScreen(
    battleResult: BattleResult?,
    onBackToSelection: () -> Unit,
    onBackToMainMenu: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Battle Result") },
                navigationIcon = {
                    IconButton(onClick = onBackToSelection) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onBackToMainMenu) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (battleResult == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No battle results available")
            }
        } else {
            BattleResultContent(
                battleResult = battleResult,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun BattleResultContent(
    battleResult: BattleResult,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Result banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = when (battleResult) {
                        is BattleResult.Victory -> "VICTORY"
                        is BattleResult.Draw -> "DRAW"
                    },
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = when (battleResult) {
                        is BattleResult.Victory ->
                            "${battleResult.winner.first.name} (${battleResult.winner.second.name}) wins!"
                        is BattleResult.Draw ->
                            "The battle ends in a draw!"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Combatants with scores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (battleResult) {
                is BattleResult.Victory -> {
                    CombatantCard(
                        characterName = battleResult.winner.first.name,
                        keyName = battleResult.winner.second.name,
                        imagePath = battleResult.winner.second.imagePath,
                        score = battleResult.winnerScore,
                        isWinner = true,
                        modifier = Modifier.weight(1f)
                    )

                    CombatantCard(
                        characterName = battleResult.loser.first.name,
                        keyName = battleResult.loser.second.name,
                        imagePath = battleResult.loser.second.imagePath,
                        score = battleResult.loserScore,
                        isWinner = false,
                        modifier = Modifier.weight(1f)
                    )
                }

                is BattleResult.Draw -> {
                    CombatantCard(
                        characterName = battleResult.left.first.name,
                        keyName = battleResult.left.second.name,
                        imagePath = battleResult.left.second.imagePath,
                        score = battleResult.leftScore,
                        isWinner = false,
                        modifier = Modifier.weight(1f)
                    )

                    CombatantCard(
                        characterName = battleResult.right.first.name,
                        keyName = battleResult.right.second.name,
                        imagePath = battleResult.right.second.imagePath,
                        score = battleResult.rightScore,
                        isWinner = false,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Stats comparison (expandable)
        var showStats by remember { mutableStateOf(false) }

        OutlinedButton(
            onClick = { showStats = !showStats },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showStats) "Hide Stats Comparison" else "Show Stats Comparison")
        }

        AnimatedVisibility(
            visible = showStats,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Stats Comparison",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Here we would add the stats comparison details
                    // For brevity, I'm just showing a placeholder
                    Text("Detailed stats comparison would go here")
                }
            }
        }
    }
}

@Composable
private fun CombatantCard(
    characterName: String,
    keyName: String,
    imagePath: String,
    score: Int,
    isWinner: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        border = if (isWinner) {
            ButtonDefaults.outlinedButtonBorder
        } else {
            null
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            // Character image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imagePath)
                    .crossfade(true)
                    .build(),
                contentDescription = "$characterName ($keyName)",
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Character name
            Text(
                text = characterName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Key name
            Text(
                text = keyName,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Score with label
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Score: ",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = score.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isWinner) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }

            // Winner indicator
            if (isWinner) {
                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "WINNER",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}