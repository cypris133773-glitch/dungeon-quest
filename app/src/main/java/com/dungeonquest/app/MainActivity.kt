package com.dungeonquest.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dungeonquest.app.ui.screens.*
import com.dungeonquest.app.ui.theme.DungeonQuestTheme
import com.dungeonquest.app.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DungeonQuestTheme {
                DungeonQuestApp()
            }
        }
    }
}

object Routes {
    const val MAIN_MENU = "main_menu"
    const val CHARACTER_CREATION = "character_creation"
    const val GAME = "game"
    const val COMBAT = "combat"
    const val INVENTORY = "inventory"
    const val DICE_ROLLER = "dice_roller"
}

@Composable
fun DungeonQuestApp() {
    val navController = rememberNavController()
    val viewModel: GameViewModel = viewModel()
    val gameState by viewModel.gameState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.MAIN_MENU
    ) {
        composable(Routes.MAIN_MENU) {
            MainMenuScreen(
                onNewGame = {
                    navController.navigate(Routes.CHARACTER_CREATION)
                },
                onContinue = if (gameState.gameStarted) {
                    { navController.navigate(Routes.GAME) }
                } else null,
                onDiceRoller = {
                    navController.navigate(Routes.DICE_ROLLER)
                }
            )
        }

        composable(Routes.CHARACTER_CREATION) {
            CharacterCreationScreen(
                onCreateCharacter = { name, race, charClass ->
                    viewModel.startNewGame(name, race, charClass)
                    navController.navigate(Routes.GAME) {
                        popUpTo(Routes.MAIN_MENU) { inclusive = false }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.GAME) {
            GameScreen(
                viewModel = viewModel,
                onNavigateToCombat = {
                    navController.navigate(Routes.COMBAT)
                },
                onNavigateToInventory = {
                    navController.navigate(Routes.INVENTORY)
                },
                onGameOver = {
                    viewModel.resetGame()
                    navController.navigate(Routes.MAIN_MENU) {
                        popUpTo(Routes.MAIN_MENU) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.COMBAT) {
            CombatScreen(
                viewModel = viewModel,
                onCombatEnd = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.INVENTORY) {
            InventoryScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.DICE_ROLLER) {
            DiceRollerScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
