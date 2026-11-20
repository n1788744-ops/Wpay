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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.GameItem
import com.example.ui.components.CompactHorizontalGameCard
import com.example.ui.components.GameGridCard
import com.example.ui.theme.CloudBg
import com.example.ui.theme.CloudBorder
import com.example.ui.theme.CloudSurface
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VkBlue
import com.example.viewmodel.CatalogUiState

@Composable
fun LibraryScreen(
    uiState: CatalogUiState,
    onGameClick: (GameItem) -> Unit,
    onPlayGame: (GameItem) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CloudBg)
            .testTag("library_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Моё облако",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
                Text(
                    text = "Быстрый возврат к запущенным 3D-играм и сохранённому избранному.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // Recent section
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.History, contentDescription = null, tint = CyberEmerald)
                Text(
                    text = "Недавние запуски (${uiState.recentGames.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
            }
        }

        if (uiState.recentGames.isEmpty()) {
            item {
                EmptySectionCard(
                    title = "Вы ещё не запускали игры",
                    description = "Откройте любую игру из каталога — она запустится в браузере мгновенно и появится здесь."
                )
            }
        } else {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(uiState.recentGames, key = { "lib_recent_${it.id}" }) { game ->
                        CompactHorizontalGameCard(
                            game = game,
                            onClick = { onPlayGame(game) }
                        )
                    }
                }
            }
        }

        // Favorites section
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.Favorite, contentDescription = null, tint = Color(0xFFFF4D6D))
                Text(
                    text = "Избранное (${uiState.favoriteGames.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
            }
        }

        if (uiState.favoriteGames.isEmpty()) {
            item {
                EmptySectionCard(
                    title = "Список избранного пуст",
                    description = "Нажмите на сердце на карточке игры, чтобы добавить её в быстрый доступ."
                )
            }
        } else {
            items(uiState.favoriteGames.chunked(2)) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { game ->
                        GameGridCard(
                            game = game,
                            isFavorite = true,
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
}

@Composable
private fun EmptySectionCard(title: String, description: String) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = CloudSurface,
        border = BorderStroke(1.dp, CloudBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleSmall, color = TextPrimary)
            Text(text = description, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }
    }
}
