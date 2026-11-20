package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val CloudPlayColorScheme = darkColorScheme(
    primary = VkBlue,
    onPrimary = TextPrimary,
    primaryContainer = VkBlueDeep,
    onPrimaryContainer = TextPrimary,
    secondary = CyberEmerald,
    onSecondary = CloudBg,
    secondaryContainer = Color(0xFF0A332B),
    onSecondaryContainer = CyberEmerald,
    tertiary = RtxViolet,
    onTertiary = TextPrimary,
    background = CloudBg,
    onBackground = TextPrimary,
    surface = CloudSurface,
    onSurface = TextPrimary,
    surfaceVariant = CloudSurfaceHigh,
    onSurfaceVariant = TextSecondary,
    outline = CloudBorder,
    error = DangerRed,
    onError = TextPrimary
)

private val CloudPlayShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun CloudPlayTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CloudPlayColorScheme,
        typography = CloudPlayTypography,
        shapes = CloudPlayShapes,
        content = content
    )
}
