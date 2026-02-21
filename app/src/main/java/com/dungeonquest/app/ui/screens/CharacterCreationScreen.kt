package com.dungeonquest.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dungeonquest.app.model.*
import com.dungeonquest.app.ui.components.StatsDisplay
import com.dungeonquest.app.ui.theme.*

@Composable
fun CharacterCreationScreen(
    onCreateCharacter: (String, Race, CharacterClass) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedRace by remember { mutableStateOf(Race.MENSCH) }
    var selectedClass by remember { mutableStateOf(CharacterClass.KRIEGER) }
    var step by remember { mutableIntStateOf(0) } // 0=name, 1=race, 2=class, 3=confirm

    val combinedStats = selectedClass.baseStats + selectedRace.statBonus

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Header
        Text(
            text = "⚔️ Charakter erstellen",
            style = MaterialTheme.typography.displayMedium,
            color = GoldPrimary,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Step indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            listOf("Name", "Volk", "Klasse", "Bestätigen").forEachIndexed { index, label ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                if (index <= step) GoldPrimary else DarkSurfaceVariant
                            )
                    ) {
                        Text(
                            text = "${index + 1}",
                            color = if (index <= step) DarkBackground else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (index <= step) GoldPrimary else TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (step) {
            0 -> NameStep(name = name, onNameChange = { name = it })
            1 -> RaceStep(selectedRace = selectedRace, onRaceSelect = { selectedRace = it })
            2 -> ClassStep(selectedClass = selectedClass, onClassSelect = { selectedClass = it })
            3 -> ConfirmStep(
                name = name,
                race = selectedRace,
                charClass = selectedClass,
                stats = combinedStats
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Navigation buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = {
                    if (step > 0) step-- else onBack()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
            ) {
                Text(if (step > 0) "← Zurück" else "← Menü")
            }

            Button(
                onClick = {
                    if (step < 3) {
                        step++
                    } else {
                        onCreateCharacter(
                            name.ifBlank { "Held" },
                            selectedRace,
                            selectedClass
                        )
                    }
                },
                enabled = step != 0 || name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldDark.copy(alpha = 0.4f),
                    contentColor = GoldLight
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (step < 3) "Weiter →" else "⚔️ Abenteuer starten!")
            }
        }
    }
}

@Composable
private fun NameStep(name: String, onNameChange: (String) -> Unit) {
    Column {
        Text(
            text = "Wie heißt dein Held?",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name deines Charakters") },
            placeholder = { Text("z.B. Thorin, Elara, Kael...") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = GoldDark.copy(alpha = 0.5f),
                focusedLabelColor = GoldPrimary,
                cursorColor = GoldPrimary
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Wähle einen Namen für deinen Abenteurer. Er wird in der Geschichte verwendet.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Composable
private fun RaceStep(selectedRace: Race, onRaceSelect: (Race) -> Unit) {
    Column {
        Text(
            text = "Wähle dein Volk",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))

        Race.entries.forEach { race ->
            val raceIcon = when (race) {
                Race.MENSCH -> "👤"
                Race.ELF -> "🧝"
                Race.ZWERG -> "⛏️"
                Race.HALBLING -> "🍀"
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onRaceSelect(race) },
                colors = CardDefaults.cardColors(
                    containerColor = if (race == selectedRace)
                        GoldDark.copy(alpha = 0.2f) else DarkCard
                ),
                shape = RoundedCornerShape(12.dp),
                border = if (race == selectedRace)
                    BorderStroke(2.dp, GoldPrimary) else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = raceIcon,
                        fontSize = 32.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = race.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (race == selectedRace) GoldPrimary else TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = race.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                    if (race == selectedRace) {
                        Text("✓", color = GoldPrimary, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ClassStep(selectedClass: CharacterClass, onClassSelect: (CharacterClass) -> Unit) {
    Column {
        Text(
            text = "Wähle deine Klasse",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))

        CharacterClass.entries.forEach { charClass ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onClassSelect(charClass) },
                colors = CardDefaults.cardColors(
                    containerColor = if (charClass == selectedClass)
                        GoldDark.copy(alpha = 0.2f) else DarkCard
                ),
                shape = RoundedCornerShape(12.dp),
                border = if (charClass == selectedClass)
                    BorderStroke(2.dp, GoldPrimary) else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = charClass.icon,
                        fontSize = 32.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = charClass.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (charClass == selectedClass) GoldPrimary else TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = charClass.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Trefferwürfel: W${charClass.hitDie}",
                            style = MaterialTheme.typography.labelSmall,
                            color = BlueArcane
                        )
                    }
                    if (charClass == selectedClass) {
                        Text("✓", color = GoldPrimary, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfirmStep(
    name: String,
    race: Race,
    charClass: CharacterClass,
    stats: Stats
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Dein Held",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Portrait
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(3.dp, GoldPrimary, CircleShape)
                .background(DarkSurfaceVariant)
        ) {
            Text(text = charClass.icon, fontSize = 48.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = name.ifBlank { "Held" },
            style = MaterialTheme.typography.headlineLarge,
            color = GoldPrimary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${race.displayName} ${charClass.displayName}",
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Stats
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Attribute",
                    style = MaterialTheme.typography.titleMedium,
                    color = GoldPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                StatsDisplay(stats = stats)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Starting equipment
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Startausrüstung",
                    style = MaterialTheme.typography.titleMedium,
                    color = GoldPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Items.starterItems(charClass).forEach { item ->
                    Text(
                        text = "${item.icon} ${item.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
                Text(
                    text = "💰 10 Gold",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GoldLight,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val maxHp = charClass.hitDie + stats.conMod + 4
        Text(
            text = "❤️ Lebenspunkte: $maxHp  •  🛡️ Rüstungsklasse: ${10 + stats.dexMod}",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
