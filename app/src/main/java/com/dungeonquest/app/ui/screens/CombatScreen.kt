package com.dungeonquest.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dungeonquest.app.model.*
import com.dungeonquest.app.ui.components.*
import com.dungeonquest.app.ui.theme.*
import com.dungeonquest.app.viewmodel.GameViewModel

@Composable
fun CombatScreen(
    viewModel: GameViewModel,
    onCombatEnd: () -> Unit
) {
    val gameState by viewModel.gameState.collectAsState()
    val combat = gameState.combatState ?: run {
        onCombatEnd()
        return
    }
    val character = gameState.character

    // Pulsing red background for combat
    val infiniteTransition = rememberInfiniteTransition(label = "combat_bg")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.02f,
        targetValue = 0.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        DarkBackground,
                        RedDark.copy(alpha = pulseAlpha),
                        DarkBackground
                    )
                )
            )
    ) {
        // Combat header
        Text(
            text = "⚔️ KAMPF ⚔️",
            style = MaterialTheme.typography.titleLarge,
            color = RedHealth,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
        )

        // Enemy section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = combat.enemy.icon,
                    fontSize = 56.sp
                )
                Text(
                    text = combat.enemy.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = RedHealth,
                    fontWeight = FontWeight.Bold
                )
                if (combat.enemy.specialAbility != null && !combat.enemySpecialUsed) {
                    Text(
                        text = "⚡ Spezialfähigkeit bereit",
                        style = MaterialTheme.typography.labelSmall,
                        color = OrangeWarning
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                StatBar(
                    label = "HP",
                    current = combat.enemyCurrentHp,
                    max = combat.enemy.maxHp,
                    color = RedHealth
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "RK: ${combat.enemy.armorClass}  |  Runde: ${combat.round}",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Combat log (scrollable middle section)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = DarkSurface.copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            val scrollState = rememberScrollState()

            LaunchedEffect(combat.combatLog.size) {
                scrollState.animateScrollTo(scrollState.maxValue)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(12.dp)
            ) {
                combat.combatLog.forEach { entry ->
                    val color = when {
                        entry.startsWith("⚡") -> GoldLight
                        entry.startsWith("✓") -> GreenSuccess
                        entry.startsWith("✗") -> RedHealth.copy(alpha = 0.7f)
                        entry.startsWith("💀") -> RedHealth
                        entry.startsWith("🏆") -> GoldPrimary
                        entry.startsWith("☠") -> RedHealth
                        entry.startsWith("🛡") -> BlueArcane
                        entry.startsWith("🧪") -> GreenHeal
                        entry.startsWith("📜") -> PurpleMagic
                        entry.startsWith("💥") -> OrangeWarning
                        entry.startsWith("🏃") -> GreenSuccess
                        entry.startsWith("---") -> GoldDark
                        entry.contains("HP:") || entry.contains("Deine HP") -> TextSecondary
                        else -> TextPrimary
                    }
                    Text(
                        text = entry,
                        style = MaterialTheme.typography.bodySmall,
                        color = color,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Player info
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${character.charClass.icon} ${character.name}",
                        style = MaterialTheme.typography.titleMedium,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "RK: ${character.armorClass}  ATK: +${character.attackBonus}",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                StatBar(
                    label = "HP",
                    current = character.currentHp,
                    max = character.maxHp,
                    color = when {
                        character.currentHp <= character.maxHp / 4 -> RedHealth
                        character.currentHp <= character.maxHp / 2 -> OrangeWarning
                        else -> GreenHeal
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Action buttons
        if (combat.isOver) {
            // Combat over - show result button
            Button(
                onClick = {
                    viewModel.endCombat()
                    onCombatEnd()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (combat.playerWon) GoldDark.copy(alpha = 0.4f)
                    else RedDark.copy(alpha = 0.4f),
                    contentColor = if (combat.playerWon) GoldLight else TextPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (combat.playerWon) "🏆 Sieg! Weiter →" else "☠️ Besiegt... Weiter",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else if (combat.isPlayerTurn) {
            // Player action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CombatActionButton(
                    text = "Angriff",
                    icon = "⚔️",
                    color = RedHealth,
                    onClick = { viewModel.combatAttack() },
                    modifier = Modifier.weight(1f)
                )
                CombatActionButton(
                    text = "Verteidigen",
                    icon = "🛡️",
                    color = BlueArcane,
                    onClick = { viewModel.combatDefend() },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val potions = character.inventory.filter {
                    it.isConsumable && (it.type == ItemType.POTION || it.type == ItemType.SCROLL)
                }
                CombatActionButton(
                    text = "Item (${potions.size})",
                    icon = "🧪",
                    color = GreenHeal,
                    enabled = potions.isNotEmpty(),
                    onClick = {
                        val item = potions.firstOrNull()
                        if (item != null) viewModel.combatUseItem(item)
                    },
                    modifier = Modifier.weight(1f)
                )
                CombatActionButton(
                    text = "Fliehen",
                    icon = "🏃",
                    color = OrangeWarning,
                    onClick = { viewModel.combatFlee() },
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            // Enemy turn indicator
            Text(
                text = "⏳ ${combat.enemy.name} ist am Zug...",
                style = MaterialTheme.typography.bodyMedium,
                color = RedHealth,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
