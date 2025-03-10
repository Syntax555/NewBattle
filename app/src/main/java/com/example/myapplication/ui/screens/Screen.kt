package com.example.myapplication.ui.screens

/**
 * Represents the different screens of the app.
 */
sealed class Screen {
    object Start : Screen()
    object MainMenu : Screen()
    object Game : Screen() // New Game screen added
}
