package com.example.myapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

/**
 * Start screen that displays "Tap to Start".
 *
 * @param onStartClicked Callback triggered when the screen is tapped.
 */
@Composable
fun StartScreen(onStartClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onStartClicked() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Tap to Start", fontSize = 24.sp)
    }
}
