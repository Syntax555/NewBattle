package com.example.battlecompare.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.battlecompare.core.AppDependencies
import com.example.battlecompare.features.battle.ui.BattleResultScreen
import com.example.battlecompare.features.battle.ui.CharacterSelectionScreen
import com.example.battlecompare.features.battle.viewmodel.BattleViewModel
import com.example.battlecompare.ui.screens.MainMenuScreen
import com.example.battlecompare.ui.screens.SplashScreen

/**
 * Represents the navigation destinations in the app.
 */
sealed class NavDestination(val route: String) {
    data object Splash : NavDestination("splash")
    data object MainMenu : NavDestination("main_menu")
    data object CharacterSelection : NavDestination("character_selection")
    data object BattleResult : NavDestination("battle_result")

    // Add parameters to routes if needed
    fun withParams(vararg params: Pair<String, String>): String {
        return if (params.isEmpty()) {
            route
        } else {
            val queryParams = params.joinToString("&") { "${it.first}=${it.second}" }
            "$route?$queryParams"
        }
    }
}

/**
 * Main navigation component for the app.
 * Uses a single shared ViewModel for character selection and battle.
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDestination.Splash.route
) {
    // Create shared ViewModel for character selection and battle result screens
    val battleViewModel: BattleViewModel = viewModel(
        factory = BattleViewModel.Factory(AppDependencies.provideCharacterRepository())
    )

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash screen
        composable(NavDestination.Splash.route) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(NavDestination.MainMenu.route) {
                        popUpTo(NavDestination.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Main menu screen
        composable(NavDestination.MainMenu.route) {
            MainMenuScreen(
                onStartBattle = {
                    // Reset selections when starting a new battle
                    battleViewModel.clearSelections()
                    navController.navigate(NavDestination.CharacterSelection.route)
                },
                onSettings = {
                    // Navigate to settings if implemented
                },
                onAbout = {
                    // Show about dialog or navigate to about screen
                }
            )
        }

        // Character selection screen
        composable(NavDestination.CharacterSelection.route) {
            // Get state from ViewModel
            val uiState by battleViewModel.uiState.collectAsState()
            val leftSelection by battleViewModel.leftSelection.collectAsState()
            val rightSelection by battleViewModel.rightSelection.collectAsState()
            val filteredCharacters by battleViewModel.filteredCharacters.collectAsState()
            val availableOrigins by battleViewModel.availableOrigins.collectAsState()
            val selectedOrigin by battleViewModel.selectedOrigin.collectAsState()
            val canStartBattle by battleViewModel.canStartBattle.collectAsState()

            CharacterSelectionScreen(
                uiState = uiState,
                leftSelection = leftSelection,
                rightSelection = rightSelection,
                filteredCharacters = filteredCharacters,
                availableOrigins = availableOrigins,
                selectedOrigin = selectedOrigin,
                canStartBattle = canStartBattle,
                onSelectLeftCharacter = battleViewModel::selectLeftCharacter,
                onSelectRightCharacter = battleViewModel::selectRightCharacter,
                onOriginSelected = { origin ->
                    battleViewModel.filterCharacters(origin = origin)
                },
                onSearch = { query ->
                    battleViewModel.filterCharacters(query = query)
                },
                onBattleInitiated = {
                    navController.navigate(NavDestination.BattleResult.route)
                },
                onBackToMenu = {
                    navController.popBackStack(
                        NavDestination.MainMenu.route,
                        inclusive = false
                    )
                }
            )
        }

        // Battle result screen
        composable(NavDestination.BattleResult.route) {
            val battleResult = battleViewModel.calculateBattleOutcome()

            BattleResultScreen(
                battleResult = battleResult,
                onBackToSelection = {
                    navController.popBackStack()
                },
                onBackToMainMenu = {
                    navController.popBackStack(
                        NavDestination.MainMenu.route,
                        inclusive = false
                    )
                },
                onRematch = {
                    // Keep the same characters but recalculate
                    navController.popBackStack()
                    navController.navigate(NavDestination.BattleResult.route)
                }
            )
        }
    }
}

/**
 * A simplified screen to help with navigation testing and previews.
 */
@Composable
fun NavigationPreview() {
    AppNavigation()
}