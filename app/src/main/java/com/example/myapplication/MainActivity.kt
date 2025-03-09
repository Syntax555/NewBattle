package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.myapplication.ui.screens.MainMenu
import com.example.myapplication.ui.screens.Screen
import com.example.myapplication.ui.screens.StartScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Simple navigation state for the start screen and main menu.
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Start) }

                when (currentScreen) {
                    Screen.Start -> StartScreen { currentScreen = Screen.MainMenu }
                    Screen.MainMenu -> MainMenu()
                }
            }
        }
    }
}
