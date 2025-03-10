package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.BattleResultScreen
import com.example.myapplication.ui.screens.GameScreen
import com.example.myapplication.ui.screens.MainMenuScreen
import com.example.myapplication.ui.screens.StartScreen
import com.example.myapplication.viewmodel.CharacterViewModel

sealed class AppDestination(val route: String) {
    data object Start : AppDestination("start")
    data object MainMenu : AppDestination("main_menu")
    data object Game : AppDestination("game")
    data object BattleResult : AppDestination("battle_result")
}

@Composable
fun AppNavigation(
    startDestination: String = AppDestination.Start.route
) {
    val navController = rememberNavController()
    // Create a shared ViewModel for the whole navigation graph
    val viewModel: CharacterViewModel = viewModel(factory = CharacterViewModel.Factory())

    NavHost(navController = navController, startDestination = startDestination) {
        composable(AppDestination.Start.route) {
            StartScreen(
                onStartClicked = {
                    navController.navigate(AppDestination.MainMenu.route) {
                        popUpTo(AppDestination.Start.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestination.MainMenu.route) {
            MainMenuScreen(
                onStartGame = {
                    navController.navigate(AppDestination.Game.route)
                }
            )
        }

        composable(AppDestination.Game.route) {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val leftSelection by viewModel.leftSelection.collectAsStateWithLifecycle()
            val rightSelection by viewModel.rightSelection.collectAsStateWithLifecycle()

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