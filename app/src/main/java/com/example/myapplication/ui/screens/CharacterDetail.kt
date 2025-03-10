package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.CharacterKey
import com.example.myapplication.model.GameCharacter

@Composable
fun CharacterDetail(
    character: GameCharacter,
    key: CharacterKey,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(
            text = key.name,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Origin: ${character.origin.displayName}")
        Text(text = "Gender: ${character.gender.label}")
        Text(
            text = "Classifications: ${key.classifications.joinToString { it.label }}"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Stats: (details here)",
            style = MaterialTheme.typography.titleSmall
        )
    }
}