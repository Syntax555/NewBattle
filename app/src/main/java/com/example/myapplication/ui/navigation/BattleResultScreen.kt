// BattleResultScreen.kt (New screen)
package com.example.myapplication.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodel.BattleResult

@Composable
fun BattleResultScreen(
    battleResult: BattleResult?,
    onBackToSelection: () -> Unit,
    onBackToMainMenu: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Battle Result",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Display battle result
            if (battleResult == null) {
                Text("No battle result available")
            } else {
                when (battleResult) {
                    is BattleResult.Victory -> {
                        Text(
                            text = "${battleResult.winner.first.name} (${battleResult.winner.second.name}) WINS!",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Score display
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Final Score",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("${battleResult.winner.first.name}: ${battleResult.winnerScore}")
                                Text("${battleResult.loser.first.name}: ${battleResult.loserScore}")
                            }
                        }
                    }
                    is BattleResult.Draw -> {
                        Text(
                            text = "DRAW!",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Score display
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Final Score",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("${battleResult.left.first.name}: ${battleResult.leftScore}")
                                Text("${battleResult.right.first.name}: ${battleResult.rightScore}")
                            }
                        }
                    }
                }
            }

            // Navigation buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onBackToSelection,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Back to Selection")
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