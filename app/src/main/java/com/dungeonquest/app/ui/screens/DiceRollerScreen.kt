package com.dungeonquest.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dungeonquest.app.ui.components.D20Display
import com.dungeonquest.app.ui.theme.*
import com.dungeonquest.app.viewmodel.GameViewModel

@Composable
fun DiceRollerScreen(
    viewModel: GameViewModel,
    onBack: () -> Unit
) {
    val diceState by viewModel.diceState.collectAsState()

    LaunchedEffect(diceState.isRolling) {
        if (diceState.isRolling) {
            kotlinx.coroutines.delay(900)
            viewModel.finishRollAnimation()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Zurück",
                    tint = TextPrimary
                )
            }
            Text(
                text = "🎲 Würfel-Werkzeug",
                style = MaterialTheme.typography.headlineMedium,
                color = GoldPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Dice display
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            D20Display(
                value = diceState.lastRoll?.naturalRoll,
                isRolling = diceState.isRolling,
                size = 150
            )
        }

        // Result text
        diceState.lastRoll?.let { roll ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = roll.toDisplayString(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = when {
                        roll.isCritical -> GoldLight
                        roll.isCriticalFail -> RedHealth
                        else -> TextPrimary
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                if (roll.isCritical) {
                    Text(
                        text = "⚡ KRITISCH!",
                        color = GoldLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                if (roll.isCriticalFail) {
                    Text(
                        text = "💀 PATZER!",
                        color = RedHealth,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dice type selection
        Text(
            text = "Würfel wählen",
            style = MaterialTheme.typography.titleMedium,
            color = GoldPrimary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(4, 6, 8, 10, 12, 20, 100).forEach { sides ->
                val isSelected = diceState.selectedDie == sides
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.selectDie(sides) },
                    label = {
                        Text(
                            text = "W$sides",
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldDark.copy(alpha = 0.3f),
                        selectedLabelColor = GoldLight
                    ),
                    border = if (isSelected) {
                        BorderStroke(2.dp, GoldPrimary)
                    } else {
                        BorderStroke(1.dp, GoldDark.copy(alpha = 0.3f))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Modifier
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Modifikator: ",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary
            )
            IconButton(
                onClick = { viewModel.setModifier(diceState.modifier - 1) },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(DarkCard)
            ) {
                Text(text = "−", color = RedHealth, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                text = if (diceState.modifier >= 0) "+${diceState.modifier}" else "${diceState.modifier}",
                style = MaterialTheme.typography.headlineMedium,
                color = if (diceState.modifier >= 0) GreenSuccess else RedHealth,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = { viewModel.setModifier(diceState.modifier + 1) },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(DarkCard)
            ) {
                Text(text = "+", color = GreenSuccess, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Roll button
        Button(
            onClick = { viewModel.rollDice() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(60.dp),
            enabled = !diceState.isRolling,
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldDark.copy(alpha = 0.4f),
                contentColor = GoldLight
            ),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(
                width = 2.dp,
                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                    listOf(GoldDark, GoldPrimary, GoldDark)
                )
            )
        ) {
            Text(
                text = "🎲  WÜRFELN  🎲",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Roll history
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Verlauf",
                style = MaterialTheme.typography.titleMedium,
                color = GoldPrimary
            )
            if (diceState.rollHistory.isNotEmpty()) {
                IconButton(
                    onClick = { viewModel.clearRollHistory() },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Verlauf löschen",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(diceState.rollHistory) { roll ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = roll.toDisplayString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = when {
                                roll.isCritical -> GoldLight
                                roll.isCriticalFail -> RedHealth
                                else -> TextPrimary
                            }
                        )
                        if (roll.isCritical) {
                            Text("⚡", fontSize = 14.sp)
                        }
                        if (roll.isCriticalFail) {
                            Text("💀", fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}
