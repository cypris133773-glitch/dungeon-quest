package com.dungeonquest.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dungeonquest.app.ui.theme.*

@Composable
fun MainMenuScreen(
    onNewGame: () -> Unit,
    onContinue: (() -> Unit)? = null,
    onDiceRoller: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Animated background
        val infiniteTransition = rememberInfiniteTransition(label = "bg")
        val animOffset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(8000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "offset"
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D0D0D),
                        Color(0xFF1A0E28).copy(alpha = 0.5f + animOffset * 0.3f),
                        Color(0xFF0D0D0D)
                    )
                )
            )
            // Particle stars
            for (i in 0 until 30) {
                val x = (size.width * ((i * 37 + 13) % 100) / 100f)
                val y = (size.height * ((i * 53 + 7) % 100) / 100f)
                val alpha = (0.1f + (i % 5) * 0.1f) * (0.5f + animOffset * 0.5f)
                drawCircle(
                    color = GoldLight.copy(alpha = alpha),
                    radius = 1f + (i % 3),
                    center = Offset(x, y)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Dragon icon
            Text(
                text = "🐉",
                fontSize = 72.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Title
            Text(
                text = "DUNGEON",
                style = MaterialTheme.typography.displayLarge,
                color = GoldPrimary,
                letterSpacing = 6.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "QUEST",
                style = MaterialTheme.typography.displayMedium,
                color = GoldLight,
                letterSpacing = 8.sp,
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ein Dungeons & Dragons Abenteuer",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            // New Game button
            Button(
                onClick = onNewGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldDark.copy(alpha = 0.3f),
                    contentColor = GoldLight
                ),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(
                        listOf(GoldDark, GoldPrimary, GoldDark)
                    )
                )
            ) {
                Text(
                    text = "⚔️  Neues Abenteuer",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (onContinue != null) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextPrimary
                    )
                ) {
                    Text(
                        text = "📜  Weiterspielen",
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onDiceRoller,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TextSecondary
                )
            ) {
                Text(
                    text = "🎲  Würfel-Werkzeug",
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "v1.0  •  Inspiriert von D&D 5e",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary.copy(alpha = 0.5f)
            )
        }
    }
}
