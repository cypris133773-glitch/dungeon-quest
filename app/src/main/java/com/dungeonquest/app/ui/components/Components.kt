package com.dungeonquest.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dungeonquest.app.model.*
import com.dungeonquest.app.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

// --- Health/XP Bar ---
@Composable
fun StatBar(
    label: String,
    current: Int,
    max: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )
            Text(
                text = "$current / $max",
                style = MaterialTheme.typography.labelMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { if (max > 0) current.toFloat() / max.toFloat() else 0f },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
            color = color,
            trackColor = DarkSurface,
        )
    }
}

// --- Character Stats Display ---
@Composable
fun StatsDisplay(
    stats: Stats,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val statList = listOf(
        "STR" to stats.str,
        "GES" to stats.dex,
        "KON" to stats.con,
        "INT" to stats.int,
        "WEI" to stats.wis,
        "CHA" to stats.cha
    )

    if (compact) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            statList.forEach { (name, value) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldPrimary,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "$value",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    val mod = (value - 10) / 2
                    Text(
                        text = if (mod >= 0) "+$mod" else "$mod",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (mod >= 0) GreenSuccess else RedHealth,
                        fontSize = 10.sp
                    )
                }
            }
        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            statList.forEach { (name, value) ->
                StatBox(name = name, value = value)
            }
        }
    }
}

@Composable
fun StatBox(name: String, value: Int) {
    val mod = (value - 10) / 2
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .border(1.dp, GoldDark, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .width(44.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = GoldPrimary,
            fontSize = 10.sp
        )
        Text(
            text = "$value",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = if (mod >= 0) "+$mod" else "$mod",
            style = MaterialTheme.typography.labelSmall,
            color = if (mod >= 0) GreenSuccess else RedHealth
        )
    }
}

// --- Choice Card ---
@Composable
fun ChoiceCard(
    text: String,
    enabled: Boolean = true,
    hasSkillCheck: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) DarkCard else DarkCard.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().let {
            if (hasSkillCheck) {
                BorderStroke(1.dp, BlueArcane.copy(alpha = 0.5f))
            } else {
                BorderStroke(1.dp, GoldDark.copy(alpha = 0.3f))
            }
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (hasSkillCheck) "🎲" else "▶",
                fontSize = if (hasSkillCheck) 18.sp else 12.sp,
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) TextPrimary else TextSecondary.copy(alpha = 0.5f),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// --- D20 Dice Display ---
@Composable
fun D20Display(
    value: Int?,
    isRolling: Boolean = false,
    size: Int = 120,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isRolling) 720f else 0f,
        animationSpec = tween(
            durationMillis = if (isRolling) 800 else 0,
            easing = FastOutSlowInEasing
        ),
        label = "dice_rotation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isRolling) 1.2f else 1f,
        animationSpec = tween(
            durationMillis = if (isRolling) 400 else 200,
            easing = FastOutSlowInEasing
        ),
        label = "dice_scale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size.dp)
            .rotate(rotation)
    ) {
        Canvas(
            modifier = Modifier.size((size * scale).dp)
        ) {
            val center = Offset(this.size.width / 2, this.size.height / 2)
            val radius = this.size.minDimension / 2 * 0.9f

            // D20 shape (icosahedron face = triangle)
            val path = Path()
            val sides = 20
            val outerRadius = radius
            val points = (0 until 3).map { i ->
                val angle = Math.toRadians((i * 120.0 - 90.0))
                Offset(
                    center.x + outerRadius * cos(angle).toFloat(),
                    center.y + outerRadius * sin(angle).toFloat()
                )
            }

            // Draw hexagon-like shape for d20
            val hexPoints = (0 until 6).map { i ->
                val angle = Math.toRadians((i * 60.0 - 30.0))
                Offset(
                    center.x + outerRadius * cos(angle).toFloat(),
                    center.y + outerRadius * sin(angle).toFloat()
                )
            }

            // Outer shape
            val hexPath = Path().apply {
                moveTo(hexPoints[0].x, hexPoints[0].y)
                hexPoints.drop(1).forEach { lineTo(it.x, it.y) }
                close()
            }

            // Shadow
            drawPath(
                hexPath,
                color = Color.Black.copy(alpha = 0.4f),
                style = Fill
            )

            // Main d20 face
            val gradient = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF2C1810),
                    Color(0xFF1A0E08)
                ),
                start = Offset(0f, 0f),
                end = Offset(this.size.width, this.size.height)
            )
            drawPath(hexPath, brush = gradient, style = Fill)

            // Border
            drawPath(
                hexPath,
                color = GoldPrimary,
                style = Stroke(width = 3f)
            )

            // Inner lines
            hexPoints.forEachIndexed { i, point ->
                if (i % 2 == 0) {
                    drawLine(
                        color = GoldDark.copy(alpha = 0.4f),
                        start = center,
                        end = point,
                        strokeWidth = 1f
                    )
                }
            }
        }

        // Number on dice
        Text(
            text = value?.toString() ?: "?",
            fontSize = (size / 3).sp,
            fontWeight = FontWeight.Bold,
            color = when {
                value == 20 -> GoldLight
                value == 1 -> RedHealth
                else -> TextPrimary
            },
            textAlign = TextAlign.Center
        )
    }
}

// --- Character Portrait ---
@Composable
fun CharacterPortrait(
    charClass: CharacterClass,
    race: Race,
    modifier: Modifier = Modifier,
    size: Int = 80
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .border(2.dp, GoldPrimary, CircleShape)
            .background(DarkSurfaceVariant)
    ) {
        Text(
            text = charClass.icon,
            fontSize = (size / 2).sp
        )
    }
}

// --- Scene Image ---
@Composable
fun SceneImage(
    imageId: String,
    modifier: Modifier = Modifier
) {
    val colors = when (imageId) {
        "scene_tavern" -> listOf(Color(0xFF3E2723), Color(0xFF4E342E), Color(0xFFFF8F00))
        "scene_forest" -> listOf(Color(0xFF1B5E20), Color(0xFF2E7D32), Color(0xFF004D40))
        "scene_dungeon" -> listOf(Color(0xFF212121), Color(0xFF37474F), Color(0xFF263238))
        "scene_boss" -> listOf(Color(0xFF4A148C), Color(0xFF880E4F), Color(0xFFB71C1C))
        else -> listOf(DarkSurface, DarkSurfaceVariant, DarkCard)
    }

    val icon = when (imageId) {
        "scene_tavern" -> "🏰"
        "scene_forest" -> "🌲"
        "scene_dungeon" -> "⛓️"
        "scene_boss" -> "💀"
        else -> "🗺️"
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gradient = Brush.verticalGradient(colors)
            drawRect(brush = gradient)

            // Decorative elements
            val starCount = 15
            for (i in 0 until starCount) {
                val x = (this.size.width * ((i * 7 + 3) % 100) / 100f)
                val y = (this.size.height * ((i * 13 + 7) % 100) / 100f)
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f + (i % 3) * 0.05f),
                    radius = 1f + (i % 3),
                    center = Offset(x, y)
                )
            }

            // Vignette effect
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.6f)
                    ),
                    center = Offset(this.size.width / 2, this.size.height / 2),
                    radius = this.size.maxDimension / 1.5f
                )
            )
        }

        // Scene icon
        Text(
            text = icon,
            fontSize = 48.sp
        )
    }
}

// --- Combat Action Button ---
@Composable
fun CombatActionButton(
    text: String,
    icon: String,
    color: Color,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.2f),
            contentColor = color,
            disabledContainerColor = DarkSurface,
            disabledContentColor = TextSecondary.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (enabled) color.copy(alpha = 0.5f) else Color.Transparent)
    ) {
        Text(text = "$icon $text", fontWeight = FontWeight.SemiBold)
    }
}

// --- Item Card ---
@Composable
fun ItemCard(
    item: Item,
    isEquipped: Boolean = false,
    onEquip: (() -> Unit)? = null,
    onUse: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isEquipped) GoldDark.copy(alpha = 0.15f) else DarkCard
        ),
        shape = RoundedCornerShape(10.dp),
        border = if (isEquipped) {
            BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.icon,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 10.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    if (isEquipped) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "✓",
                            color = GoldPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 2
                )
            }

            if (item.isConsumable && onUse != null) {
                TextButton(onClick = onUse) {
                    Text("Nutzen", color = GreenHeal, fontSize = 12.sp)
                }
            } else if ((item.type == ItemType.WEAPON || item.type == ItemType.ARMOR) && onEquip != null && !isEquipped) {
                TextButton(onClick = onEquip) {
                    Text("Anlegen", color = GoldPrimary, fontSize = 12.sp)
                }
            }
        }
    }
}

// --- Chapter Header ---
@Composable
fun ChapterHeader(chapter: Int, title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "— Kapitel $chapter —",
            style = MaterialTheme.typography.labelMedium,
            color = GoldDark,
            letterSpacing = 2.sp
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = GoldPrimary,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}
