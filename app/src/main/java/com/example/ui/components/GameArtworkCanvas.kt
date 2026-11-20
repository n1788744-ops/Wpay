package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.model.CoverMotif
import com.example.model.GameItem
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GameArtworkCanvas(
    game: GameItem,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Deep atmospheric base gradient
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF0A0E1B),
                    game.primaryColor.copy(alpha = 0.42f),
                    game.secondaryColor.copy(alpha = 0.25f)
                ),
                start = Offset.Zero,
                end = Offset(w, h)
            )
        )

        // Glowing radial spotlight
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    game.secondaryColor.copy(alpha = 0.38f),
                    Color.Transparent
                ),
                center = Offset(w * 0.72f, h * 0.32f),
                radius = w * 0.65f
            ),
            radius = w * 0.65f,
            center = Offset(w * 0.72f, h * 0.32f)
        )

        when (game.motif) {
            CoverMotif.NEON_HIGHWAY -> {
                // 3D perspective road lines
                val horY = h * 0.45f
                for (i in -3..3) {
                    drawLine(
                        color = game.secondaryColor.copy(alpha = 0.45f),
                        start = Offset(w * 0.5f + i * (w * 0.04f), horY),
                        end = Offset(w * 0.5f + i * (w * 0.24f), h),
                        strokeWidth = 2f
                    )
                }
                for (row in 1..5) {
                    val t = row / 5f
                    val y = horY + t * t * (h - horY)
                    drawLine(
                        color = game.primaryColor.copy(alpha = 0.3f),
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 1.5f
                    )
                }
                drawCircle(
                    color = game.secondaryColor.copy(alpha = 0.65f),
                    radius = h * 0.16f,
                    center = Offset(w * 0.5f, horY - h * 0.05f)
                )
            }

            CoverMotif.CYBER_CITY -> {
                val buildingWidths = listOf(0.14f, 0.12f, 0.18f, 0.14f, 0.16f, 0.14f)
                val buildingHeights = listOf(0.45f, 0.65f, 0.52f, 0.78f, 0.58f, 0.42f)
                var xPos = w * 0.05f
                buildingWidths.forEachIndexed { idx, bw ->
                    val rectW = w * bw
                    val rectH = h * buildingHeights[idx]
                    drawRect(
                        color = Color(0xFF0C1222).copy(alpha = 0.85f),
                        topLeft = Offset(xPos, h - rectH),
                        size = Size(rectW * 0.88f, rectH)
                    )
                    drawRect(
                        color = if (idx % 2 == 0) game.primaryColor.copy(alpha = 0.6f)
                        else game.secondaryColor.copy(alpha = 0.55f),
                        topLeft = Offset(xPos, h - rectH),
                        size = Size(rectW * 0.88f, rectH),
                        style = Stroke(width = 1.5f)
                    )
                    xPos += rectW
                }
            }

            CoverMotif.STAR_NEBULA -> {
                val center = Offset(w * 0.68f, h * 0.42f)
                for (ring in 1..3) {
                    drawCircle(
                        color = game.secondaryColor.copy(alpha = 0.45f - ring * 0.1f),
                        radius = h * (0.14f * ring),
                        center = center,
                        style = Stroke(width = 2.5f)
                    )
                }
                val seed = game.id.hashCode()
                for (s in 0..18) {
                    val sx = ((seed * (s + 3) * 37) and 0xFF) / 255f * w
                    val sy = ((seed * (s + 7) * 53) and 0xFF) / 255f * h
                    drawCircle(Color.White.copy(alpha = 0.55f), radius = 2f, center = Offset(sx, sy))
                }
            }

            CoverMotif.FANTASY_CITADEL -> {
                val path = Path().apply {
                    moveTo(w * 0.1f, h)
                    lineTo(w * 0.32f, h * 0.45f)
                    lineTo(w * 0.48f, h * 0.62f)
                    lineTo(w * 0.66f, h * 0.25f)
                    lineTo(w * 0.88f, h * 0.68f)
                    lineTo(w, h * 0.55f)
                    lineTo(w, h)
                    close()
                }
                drawPath(path, color = Color(0xFF0A1020).copy(alpha = 0.85f))
                drawPath(path, color = game.secondaryColor.copy(alpha = 0.5f), style = Stroke(2f))
            }

            CoverMotif.TACTICAL_GRID, CoverMotif.MECH_CORE -> {
                val cx = w * 0.68f
                val cy = h * 0.46f
                val r = h * 0.28f
                val hex = Path().apply {
                    for (i in 0..5) {
                        val a = Math.toRadians((60 * i - 30).toDouble())
                        val px = cx + (r * cos(a)).toFloat()
                        val py = cy + (r * sin(a)).toFloat()
                        if (i == 0) moveTo(px, py) else lineTo(px, py)
                    }
                    close()
                }
                drawPath(hex, color = game.primaryColor.copy(alpha = 0.25f))
                drawPath(hex, color = game.secondaryColor.copy(alpha = 0.7f), style = Stroke(2.5f))
                drawLine(game.secondaryColor.copy(alpha = 0.45f), Offset(cx - r * 1.4f, cy), Offset(cx + r * 1.4f, cy), 1.5f)
                drawLine(game.secondaryColor.copy(alpha = 0.45f), Offset(cx, cy - r * 1.4f), Offset(cx, cy + r * 1.4f), 1.5f)
            }

            CoverMotif.HORROR_ABYSS -> {
                for (i in 1..4) {
                    drawArc(
                        color = game.primaryColor.copy(alpha = 0.5f - i * 0.08f),
                        startAngle = 200f,
                        sweepAngle = 140f,
                        useCenter = false,
                        topLeft = Offset(w * 0.2f - i * 14f, h * 0.22f - i * 10f),
                        size = Size(w * 0.6f + i * 28f, h * 0.7f + i * 20f),
                        style = Stroke(2f)
                    )
                }
            }

            CoverMotif.ARENA_STADIUM -> {
                drawOval(
                    color = game.secondaryColor.copy(alpha = 0.38f),
                    topLeft = Offset(w * 0.15f, h * 0.35f),
                    size = Size(w * 0.7f, h * 0.45f),
                    style = Stroke(2.5f)
                )
                drawLine(
                    color = game.primaryColor.copy(alpha = 0.5f),
                    start = Offset(w * 0.5f, h * 0.35f),
                    end = Offset(w * 0.5f, h * 0.8f),
                    strokeWidth = 2f
                )
            }
        }

        // Bottom contrast scrim for text legibility
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color(0xE6080B14)),
                startY = h * 0.38f,
                endY = h
            )
        )
    }
}
