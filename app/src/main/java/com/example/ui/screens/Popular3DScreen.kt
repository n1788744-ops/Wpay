package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.GameCatalogProvider
import com.example.model.EngineType
import com.example.model.GameItem
import com.example.ui.components.BadgePill
import com.example.ui.components.CompactHorizontalGameCard
import com.example.ui.components.GameGridCard
import com.example.ui.theme.CloudBg
import com.example.ui.theme.CloudSurface
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.RtxViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VkBlue
import com.example.viewmodel.CatalogUiState

@Composable
fun Popular3DScreen(
    uiState: CatalogUiState,
    onGameClick: (GameItem) -> Unit,
    onPlayGame: (GameItem) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val all3D = GameCatalogProvider.allGames.filter { it.isPopular3D }
    val racing3D = all3D.filter { it.engineType == EngineType.RACING_3D }
    val space3D = all3D.filter { it.engineType == EngineType.SPACE_3D }
    val fps3D = all3D.filter { it.engineType == EngineType.FPS_3D }
    val arena3D = all3D.filter { it.engineType == EngineType.ARENA_3D }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CloudBg)
            .testTag("popular_3d_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = CloudSurface,
                border = BorderStroke(1.dp, RtxViolet.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.ViewInAr, contentDescription = null, tint = CyberEmerald)
                        Text(
                            text = "Популярные 3D-игры в облаке",
                            style = MaterialTheme.typography.headlineSmall,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = "${all3D.size} оптимизированных 3D-проектов с поддержкой 120 FPS на видеокарте RTX 4070 SUPER 11999 Mb. Запускаются мгновенно в окне браузера без загрузки файлов.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        BadgePill("WebGL / 3D Canvas", VkBlue, TextPrimary)
                        BadgePill("Мин. задержка", CyberEmerald.copy(alpha = 0.2f), CyberEmerald)
                    }
                }
            }
        }

        item {
            ShowcaseSection(
                title = "Гонки 3D и Высокая скорость",
                subtitle = "Динамичный 3D-драйв и нитро-ускорение",
                games = racing3D,
                onGameClick = onGameClick,
                onPlayGame = onPlayGame
            )
        }

        item {
            ShowcaseSection(
                title = "Шутеры и Тактика 3D (Raycaster)",
                subtitle = "От первого лица с минимальной задержкой",
                games = fps3D,
                onGameClick = onGameClick,
                onPlayGame = onPlayGame
            )
        }

        item {
            ShowcaseSection(
                title = "Космические 3D-экшены",
                subtitle = "Звёздные бои и полная свобода прицела",
                games = space3D,
                onGameClick = onGameClick,
                onPlayGame = onPlayGame
            )
        }

        item {
            ShowcaseSection(
                title = "RPG и Изометрические 3D-арены",
                subtitle = "Магия, тактика и командные сражения",
                games = arena3D,
                onGameClick = onGameClick,
                onPlayGame = onPlayGame
            )
        }

        item {
            Text(
                text = "Лидеры онлайна прямо сейчас",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )
        }

        items(all3D.sortedByDescending { it.playersOnline }.take(12).chunked(2)) { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                pair.forEach { game ->
                    GameGridCard(
                        game = game,
                        isFavorite = uiState.favoriteIds.contains(game.id),
                        onCardClick = { onGameClick(game) },
                        onPlayClick = { onPlayGame(game) },
                        onFavoriteClick = { onToggleFavorite(game.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ShowcaseSection(
    title: String,
    subtitle: String,
    games: List<GameItem>,
    onGameClick: (GameItem) -> Unit,
    onPlayGame: (GameItem) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
            games.firstOrNull()?.let { first ->
                Button(
                    onClick = { onPlayGame(first) },
                    colors = ButtonDefaults.buttonColors(containerColor = VkBlue, contentColor = TextPrimary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Старт", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(games.take(10), key = { "3d_${it.id}" }) { game ->
                CompactHorizontalGameCard(
                    game = game,
                    onClick = { onGameClick(game) }
                )
            }
        }
    }
}
