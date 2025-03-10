// AppNavigation.kt (simplified)
package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.BattleResultScreen
import com.example.myapplication.ui.screens.GameScreen
import com.example.myapplication.ui.screens.MainMenu
import com.example.myapplication.ui.screens.StartScreen
import com.example.myapplication.viewmodel.CharacterViewModel

/**
 * Navigation destinations.
 */
sealed class AppDestination(val route: String) {
    object Start : AppDestination("start")
    object MainMenu : AppDestination("main_menu")
    object Game : AppDestination("game")
    object BattleResult : AppDestination("battle_result")
}

/**
 * Main navigation component.
 */
@Composable
fun AppNavigation(
    viewModel: CharacterViewModel,
    startDestination: String = AppDestination.Start.route
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        // Start Screen
        composable(AppDestination.Start.route) {
            StartScreen(
                onStartClicked = {
                    navController.navigate(AppDestination.MainMenu.route) {
                        popUpTo(AppDestination.Start.route) { inclusive = true }
                    }
                }
            )
        }

        // Main Menu
        composable(AppDestination.MainMenu.route) {
            MainMenu(
                onStartGame = {
                    navController.navigate(AppDestination.Game.route)
                }
            )
        }

        // Game Screen
        composable(AppDestination.Game.route) {
            val uiState by viewModel.uiState.collectAsState()
            val leftSelection by viewModel.leftSelection.collectAsState()
            val rightSelection by viewModel.rightSelection.collectAsState()

            GameScreen(
                uiState = uiState,
                leftSelection = leftSelection,
                rightSelection = rightSelection,
                onSelectLeftCharacter = viewModel::selectLeftCharacter,
                onSelectRightCharacter = viewModel::selectRightCharacter,
                onBackToMenu = {
                    navController.popBackStack(
                        AppDestination.MainMenu.route,
                        inclusive = false
                    )
                },
                onBattleInitiated = {
                    navController.navigate(AppDestination.BattleResult.route)
                }
            )
        }

        // Battle Result
        composable(AppDestination.BattleResult.route) {
            val battleResult = viewModel.calculateBattleOutcome()

            BattleResultScreen(
                battleResult = battleResult,
                onBackToSelection = {
                    navController.popBackStack()
                },
                onBackToMainMenu = {
                    navController.popBackStack(
                        AppDestination.MainMenu.route,
                        inclusive = false
                    )
                }
            )
        }
    }
}