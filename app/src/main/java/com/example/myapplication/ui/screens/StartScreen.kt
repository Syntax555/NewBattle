package com.example.myapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun StartScreen(onStartClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics { role = Role.Button }
            .clickable(
                onClickLabel = "Start",
                onClick = onStartClicked
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Tap to Start",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}