package com.example.battlecompare.features.battle.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.battlecompare.features.battle.viewmodel.BattleResult
import com.example.battlecompare.ui.components.LoadingIndicator
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * Optimized battle result screen with engaging animations.
 * Focuses on showing the important information with minimal resource usage.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleResultScreen(
    battleResult: BattleResult?,
    onBackToSelection: () -> Unit,
    onBackToMainMenu: () -> Unit,
    onRematch: () -> Unit
) {
    // Animation states
    var showResult by remember { mutableStateOf(false) }
    var showDetails by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }

    // Trigger animations in sequence
    LaunchedEffect(key1 = battleResult) {
        // Reset animations when result changes
        showResult = false
        showDetails = false
        showButtons = false

        // Start animations in sequence
        delay(300)
        showResult = true
        delay(500)
        showDetails = true
        delay(300)
        showButtons = true
    }

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

                    IconButton(onClick = onRematch) {
                        Icon(Icons.Default.Refresh, contentDescription = "Rematch")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (battleResult == null) {
                // Show loading if result is not available
                LoadingIndicator(message = "Calculating battle result...")
            } else {
                // Content with battle result
                BattleResultContent(
                    battleResult = battleResult,
                    showResult = showResult,
                    showDetails = showDetails,
                    showButtons = showButtons,
                    onBackToSelection = onBackToSelection,
                    onBackToMainMenu = onBackToMainMenu,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
        }
    }
}

/**
 * Main content of the battle result screen
 */
@Composable
private fun BattleResultContent(
    battleResult: BattleResult,
    showResult: Boolean,
    showDetails: Boolean,
    showButtons: Boolean,
    onBackToSelection: () -> Unit,
    onBackToMainMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Animated result banner
        AnimatedVisibility(
            visible = showResult,
            enter = fadeIn(tween(500)) + expandVertically(tween(500))
        ) {
            ResultBanner(battleResult = battleResult)
        }

        // Combatants comparison with animation
        AnimatedVisibility(
            visible = showDetails,
            enter = fadeIn(tween(500)) + slideInHorizontally(
                initialOffsetX = { it / 2 },
                animationSpec = tween(500)
            )
        ) {
            CombatantsComparison(battleResult = battleResult)
        }

        // Spacer to push buttons to bottom
        Spacer(modifier = Modifier.weight(1f))

        // Navigation buttons with animation
        AnimatedVisibility(
            visible = showButtons,
            enter = fadeIn(tween(300)) + slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(300)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = onBackToSelection,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("New Battle")
                }

                Button(
                    onClick = onBackToMainMenu,
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Main Menu")
                }
            }
        }
    }
}

/**
 * Result banner showing victory/draw status
 */
@Composable
private fun ResultBanner(battleResult: BattleResult) {
    // Pulsating animation for the banner
    val pulsateAnimation = rememberInfiniteTransition(label = "pulsate")
    val scale by pulsateAnimation.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsate"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .scale(scale),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (battleResult) {
                is BattleResult.Victory -> MaterialTheme.colorScheme.primaryContainer
                is BattleResult.Draw -> MaterialTheme.colorScheme.secondaryContainer
            }
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
                color = when (battleResult) {
                    is BattleResult.Victory -> MaterialTheme.colorScheme.onPrimaryContainer
                    is BattleResult.Draw -> MaterialTheme.colorScheme.onSecondaryContainer
                }
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
                color = when (battleResult) {
                    is BattleResult.Victory -> MaterialTheme.colorScheme.onPrimaryContainer
                    is BattleResult.Draw -> MaterialTheme.colorScheme.onSecondaryContainer
                }
            )
        }
    }
}

/**
 * Combatants comparison section showing both characters and their scores
 */
@Composable
private fun CombatantsComparison(battleResult: BattleResult) {
    // Animated score counters
    val (leftCharacter, rightCharacter) = when (battleResult) {
        is BattleResult.Victory -> {
            battleResult.winner.first to battleResult.loser.first
        }
        is BattleResult.Draw -> {
            battleResult.left.first to battleResult.right.first
        }
    }

    val (leftKey, rightKey) = when (battleResult) {
        is BattleResult.Victory -> {
            battleResult.winner.second to battleResult.loser.second
        }
        is BattleResult.Draw -> {
            battleResult.left.second to battleResult.right.second
        }
    }

    val (leftScore, rightScore) = when (battleResult) {
        is BattleResult.Victory -> {
            battleResult.winnerScore to battleResult.loserScore
        }
        is BattleResult.Draw -> {
            battleResult.leftScore to battleResult.rightScore
        }
    }

    val isLeftWinner = when (battleResult) {
        is BattleResult.Victory -> battleResult.winner == Pair(leftCharacter, leftKey)
        is BattleResult.Draw -> false
    }

    val isRightWinner = when (battleResult) {
        is BattleResult.Victory -> battleResult.winner == Pair(rightCharacter, rightKey)
        is BattleResult.Draw -> false
    }

    // Animated score counters
    var leftAnimatedScore by remember { mutableStateOf(0) }
    var rightAnimatedScore by remember { mutableStateOf(0) }

    // Animate the scores
    LaunchedEffect(leftScore, rightScore) {
        // Reset counters
        leftAnimatedScore = 0
        rightAnimatedScore = 0

        // Determine animation duration based on score
        val maxScore = maxOf(leftScore, rightScore)
        val duration = 1500 // 1.5 seconds total

        // Animate scores counting up
        val leftStep = leftScore / (duration / 16f)
        val rightStep = rightScore / (duration / 16f)

        var elapsed = 0
        while (leftAnimatedScore < leftScore || rightAnimatedScore < rightScore) {
            delay(16) // ~60fps
            elapsed += 16

            leftAnimatedScore = minOf((leftStep * elapsed).roundToInt(), leftScore)
            rightAnimatedScore = minOf((rightStep * elapsed).roundToInt(), rightScore)

            if (elapsed >= duration) break
        }

        // Ensure final values match exactly
        leftAnimatedScore = leftScore
        rightAnimatedScore = rightScore
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Left combatant
        CombatantCard(
            characterName = leftCharacter.name,
            keyName = leftKey.name,
            imagePath = leftKey.imagePath,
            score = leftAnimatedScore,
            isWinner = isLeftWinner,
            modifier = Modifier.weight(1f)
        )

        // VS divider
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(vertical = 64.dp)
        ) {
            Text(
                text = "VS",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        // Right combatant
        CombatantCard(
            characterName = rightCharacter.name,
            keyName = rightKey.name,
            imagePath = rightKey.imagePath,
            score = rightAnimatedScore,
            isWinner = isRightWinner,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Card showing a combatant's details
 */
@Composable
private fun CombatantCard(
    characterName: String,
    keyName: String,
    imagePath: String,
    score: Int,
    isWinner: Boolean,
    modifier: Modifier = Modifier
) {
    // Animation for winner highlight
    val winnerAnimation = rememberInfiniteTransition(label = "winner")
    val borderWidth by winnerAnimation.animateFloat(
        initialValue = 1f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border"
    )

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        border = if (isWinner) {
            ButtonDefaults.outlinedButtonBorder.copy(
                width = borderWidth.dp
            )
        } else null
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
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
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

            Spacer(modifier = Modifier.height(8.dp))

            // Score with animation
            ScoreDisplay(
                score = score,
                isWinner = isWinner
            )

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
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * Animated score display component
 */
@Composable
private fun ScoreDisplay(
    score: Int,
    isWinner: Boolean
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isWinner)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = score.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = if (isWinner)
                MaterialTheme.colorScheme.onPrimaryContainer
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}