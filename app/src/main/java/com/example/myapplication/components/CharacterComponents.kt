// CharacterComponents.kt
package com.example.myapplication.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.foundation.layout.ExperimentalLayoutApi
// Import the stable version
import com.example.myapplication.core.model.CharacterKey
import com.example.myapplication.core.model.Classification
import com.example.myapplication.core.model.GameCharacter

/**
 * Enhanced character card component with expandable stats section.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CharacterCard(
    character: GameCharacter,
    key: CharacterKey,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Character image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(key.imagePath)
                    .crossfade(true)
                    .build(),
                contentDescription = key.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )

            // Basic info section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = key.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Origin and gender
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Origin: ${character.origin.displayName}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Gender: ${character.gender.label}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Classifications
                val classifications = key.classifications.toList()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    classifications.take(3).forEach { classification ->
                        ClassificationChip(
                            classification = classification,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }
                }
                if (classifications.size > 3) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        classifications.drop(3).take(3).forEach { classification ->
                            ClassificationChip(
                                classification = classification,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Expand/collapse button for stats
                Button(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = if (expanded) "Hide Stats" else "Show Stats")
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand/Collapse",
                        modifier = Modifier.rotate(rotationState)
                    )
                }
            }

            // Expandable stats section
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CharacterStats(statTiers = key.statTiers)
            }
        }
    }
}

/**
 * Classification chip component.
 */
@Composable
fun ClassificationChip(
    classification: Classification,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.padding(2.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
    ) {
        Text(
            text = classification.label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * Character stats display component.
 */
@Composable
fun CharacterStats(statTiers: StatTiers) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Tier label
        StatRow(
            label = "Tier",
            value = "${statTiers.tier.value.label} ${statTiers.tier.modifier?.symbol ?: ""}"
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Main stats
        StatRow(
            label = "Attack Potency",
            value = "${statTiers.attackPotency.value.label} ${statTiers.attackPotency.modifier?.symbol ?: ""}"
        )

        StatRow(
            label = "Speed",
            value = "${statTiers.speed.value.label} ${statTiers.speed.modifier?.symbol ?: ""}"
        )

        StatRow(
            label = "Lifting Strength",
            value = "${statTiers.liftingStrength.value.label} ${statTiers.liftingStrength.modifier?.symbol ?: ""}"
        )

        StatRow(
            label = "Striking Strength",
            value = "${statTiers.strikingStrength.value.label} ${statTiers.strikingStrength.modifier?.symbol ?: ""}"
        )

        StatRow(
            label = "Durability",
            value = "${statTiers.durability.value.label} ${statTiers.durability.modifier?.symbol ?: ""}"
        )

        StatRow(
            label = "Intelligence",
            value = "${statTiers.intelligence.value.label} ${statTiers.intelligence.modifier?.symbol ?: ""}"
        )

        StatRow(
            label = "Range",
            value = "${statTiers.range.value.label} ${statTiers.range.modifier?.symbol ?: ""}"
        )

        StatRow(
            label = "Stamina",
            value = "${statTiers.stamina.value.label} ${statTiers.stamina.modifier?.symbol ?: ""}"
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Total score
        StatRow(
            label = "Total Score",
            value = statTiers.totalScore.toString(),
            highlight = true
        )
    }
}

/**
 * Individual stat row component.
 */
@Composable
fun StatRow(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .let {
                if (highlight) {
                    it.background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
                        .padding(4.dp)
                } else {
                    it
                }
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal
            )
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (highlight) MaterialTheme.colorScheme.primary else Color.Unspecified
        )
    }
}