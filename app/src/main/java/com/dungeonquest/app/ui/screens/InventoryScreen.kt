package com.dungeonquest.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dungeonquest.app.model.*
import com.dungeonquest.app.ui.components.*
import com.dungeonquest.app.ui.theme.*
import com.dungeonquest.app.viewmodel.GameViewModel

@Composable
fun InventoryScreen(
    viewModel: GameViewModel,
    onBack: () -> Unit
) {
    val gameState by viewModel.gameState.collectAsState()
    val character = gameState.character
    val lastLog by viewModel.lastActionLog.collectAsState()

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
                text = "🎒 Inventar",
                style = MaterialTheme.typography.headlineMedium,
                color = GoldPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "💰 ${character.gold} Gold",
                style = MaterialTheme.typography.titleMedium,
                color = GoldLight
            )
        }

        // Action log
        if (lastLog.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = GreenHeal.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    lastLog.forEach { log ->
                        Text(
                            text = log,
                            style = MaterialTheme.typography.bodySmall,
                            color = GreenHeal
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // Character summary
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CharacterPortrait(
                            charClass = character.charClass,
                            race = character.race,
                            size = 60
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = character.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${character.race.displayName} ${character.charClass.displayName} (Lv.${character.level})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    StatBar(
                        label = "❤️ Lebenspunkte",
                        current = character.currentHp,
                        max = character.maxHp,
                        color = RedHealth
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StatBar(
                        label = "⭐ Erfahrung",
                        current = character.xp,
                        max = character.xpToNextLevel,
                        color = BlueArcane
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    StatsDisplay(stats = character.stats)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "⚔️ ATK: +${character.attackBonus}",
                            style = MaterialTheme.typography.labelLarge,
                            color = RedHealth
                        )
                        Text(
                            text = "🛡️ RK: ${character.armorClass}",
                            style = MaterialTheme.typography.labelLarge,
                            color = BlueArcane
                        )
                        Text(
                            text = "💎 W${character.weaponDamage} DMG",
                            style = MaterialTheme.typography.labelLarge,
                            color = PurpleMagic
                        )
                    }
                }
            }

            // Equipped items
            Text(
                text = "Ausgerüstet",
                style = MaterialTheme.typography.titleMedium,
                color = GoldPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
            )

            if (character.equippedWeapon != null) {
                ItemCard(
                    item = character.equippedWeapon,
                    isEquipped = true
                )
            }
            if (character.equippedArmor != null) {
                ItemCard(
                    item = character.equippedArmor,
                    isEquipped = true
                )
            }

            // Inventory items
            val unequippedItems = character.inventory.filter {
                it != character.equippedWeapon && it != character.equippedArmor
            }

            Text(
                text = "Rucksack (${unequippedItems.size} Gegenstände)",
                style = MaterialTheme.typography.titleMedium,
                color = GoldPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)
            )

            if (unequippedItems.isEmpty()) {
                Text(
                    text = "Dein Rucksack ist leer.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                unequippedItems.forEach { item ->
                    ItemCard(
                        item = item,
                        isEquipped = false,
                        onEquip = if (item.type == ItemType.WEAPON || item.type == ItemType.ARMOR) {
                            { viewModel.equipItem(item) }
                        } else null,
                        onUse = if (item.isConsumable) {
                            { viewModel.useItem(item) }
                        } else null
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
