package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.GameItem
import com.example.model.StreamSettings
import com.example.ui.theme.CloudBorder
import com.example.ui.theme.CloudSurface
import com.example.ui.theme.CloudSurfaceHigh
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.RtxViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VkBlue
import com.example.ui.theme.WarnAmber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GameDetailSheet(
    game: GameItem,
    isFavorite: Boolean,
    streamSettings: StreamSettings,
    onDismiss: () -> Unit,
    onPlayInAppBrowser: () -> Unit,
    onOpenExternalBrowser: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CloudSurface,
        contentColor = TextPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(156.dp)
                    .clip(MaterialTheme.shapes.large)
            ) {
                GameArtworkCanvas(game = game)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        BadgePill(
                            text = game.genre.labelRu,
                            background = VkBlue,
                            contentColor = TextPrimary
                        )
                        if (game.rtxSupported) {
                            BadgePill(
                                text = "RTX 3D",
                                background = RtxViolet,
                                contentColor = TextPrimary
                            )
                        }
                    }
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("sheet_fav_btn")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = stringResource(
                                if (isFavorite) R.string.action_favorite_remove else R.string.action_favorite_add
                            ),
                            tint = if (isFavorite) Color(0xFFFF4D6D) else TextPrimary
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = WarnAmber, modifier = Modifier.size(16.dp))
                    Text("${game.rating}", style = MaterialTheme.typography.labelLarge, color = TextPrimary)
                    Text("•", color = TextSecondary)
                    Text("${game.playersOnline} игроков в облаке", style = MaterialTheme.typography.labelMedium, color = CyberEmerald)
                }
            }

            Text(
                text = game.title,
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )

            Text(
                text = game.description,
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                game.tags.forEach { tag ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CloudSurfaceHigh,
                        border = BorderStroke(1.dp, CloudBorder)
                    ) {
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.labelMedium,
                            color = TextPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            // Server hardware & stream preset summary
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = CloudSurfaceHigh,
                border = BorderStroke(1.dp, VkBlue.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.Memory, contentDescription = null, tint = CyberEmerald, modifier = Modifier.size(18.dp))
                        Text(
                            text = "Ryzen 9 5900X • RTX 4070 SUPER 11999 Mb • 32 Gb",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextPrimary
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.Speed, contentDescription = null, tint = VkBlue, modifier = Modifier.size(18.dp))
                        Text(
                            text = "Поток: ${streamSettings.resolution} • ${streamSettings.targetFps} FPS • ${streamSettings.codec} • ~${game.estimatedPingMs} мс",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            Button(
                onClick = onPlayInAppBrowser,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("sheet_play_browser_btn"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VkBlue, contentColor = TextPrimary)
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.action_play_now),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            if (streamSettings.customStreamBaseUrl.isNotBlank()) {
                OutlinedButton(
                    onClick = onOpenExternalBrowser,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("sheet_external_browser_btn"),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, CloudBorder)
                ) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, tint = CyberEmerald, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.action_open_external_browser),
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}
