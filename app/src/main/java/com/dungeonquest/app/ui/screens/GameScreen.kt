package com.dungeonquest.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dungeonquest.app.model.*
import com.dungeonquest.app.ui.components.*
import com.dungeonquest.app.ui.theme.*
import com.dungeonquest.app.viewmodel.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onNavigateToCombat: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onGameOver: () -> Unit
) {
    val gameState by viewModel.gameState.collectAsState()
    val lastLog by viewModel.lastActionLog.collectAsState()
    val character = gameState.character
    val currentNode = viewModel.getCurrentNode()

    // Navigate to combat if combat started
    LaunchedEffect(gameState.combatState) {
        if (gameState.combatState != null) {
            onNavigateToCombat()
        }
    }

    if (currentNode == null) return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Top bar with character info
        CharacterTopBar(
            character = character,
            onInventoryClick = onNavigateToInventory
        )

        // Main content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Scene image
            SceneImage(imageId = currentNode.imageId)

            Spacer(modifier = Modifier.height(12.dp))

            // Chapter header
            ChapterHeader(chapter = currentNode.chapter, title = currentNode.title)

            // Action log (if any)
            if (lastLog.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = DarkSurfaceVariant.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        lastLog.forEach { log ->
                            Text(
                                text = log,
                                style = MaterialTheme.typography.bodySmall,
                                color = when {
                                    log.startsWith("✓") -> GreenSuccess
                                    log.startsWith("✗") -> RedHealth
                                    log.startsWith("+") -> GoldPrimary
                                    log.startsWith("⬆") -> BlueArcane
                                    else -> TextSecondary
                                },
                                modifier = Modifier.padding(vertical = 1.dp)
                            )
                        }
                    }
                }
            }

            // Story text
            Text(
                text = currentNode.text,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                lineHeight = 26.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Choices or ending
            if (currentNode.isEnding) {
                // Game ending
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onGameOver,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldDark.copy(alpha = 0.4f),
                        contentColor = GoldLight
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "🏰 Zurück zum Hauptmenü",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else if (currentNode.combat != null) {
                // Combat will be triggered
                Text(
                    text = "⚔️ Ein Kampf beginnt!",
                    style = MaterialTheme.typography.titleLarge,
                    color = RedHealth,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            } else {
                // Story choices
                currentNode.choices.forEach { choice ->
                    val canChoose = viewModel.canMakeChoice(choice)
                    val hasSkillCheck = choice.skillCheck != null

                    ChoiceCard(
                        text = buildString {
                            append(choice.text)
                            if (choice.skillCheck != null) {
                                append(" [SG ${choice.skillCheck.dc}]")
                            }
                            if (!canChoose && choice.requiredItem != null) {
                                append(" (Gegenstand fehlt)")
                            }
                        },
                        enabled = canChoose,
                        hasSkillCheck = hasSkillCheck,
                        onClick = { viewModel.makeChoice(choice) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun CharacterTopBar(
    character: PlayerCharacter,
    onInventoryClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Character info
                Text(
                    text = "${character.charClass.icon} ${character.name}",
                    style = MaterialTheme.typography.titleMedium,
                    color = GoldPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Lv.${character.level}",
                    style = MaterialTheme.typography.labelLarge,
                    color = BlueArcane,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "💰 ${character.gold}",
                    style = MaterialTheme.typography.labelLarge,
                    color = GoldLight,
                    modifier = Modifier.padding(end = 8.dp)
                )
                IconButton(
                    onClick = onInventoryClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Backpack,
                        contentDescription = "Inventar",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // HP Bar
            StatBar(
                label = "❤️ LP",
                current = character.currentHp,
                max = character.maxHp,
                color = RedHealth
            )

            Spacer(modifier = Modifier.height(4.dp))

            // XP Bar
            StatBar(
                label = "⭐ EP",
                current = character.xp,
                max = character.xpToNextLevel,
                color = BlueArcane
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Compact stats
            StatsDisplay(stats = character.stats, compact = true)
        }
    }
}
