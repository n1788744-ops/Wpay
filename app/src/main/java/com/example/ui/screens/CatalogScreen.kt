package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.GameGenre
import com.example.model.GameItem
import com.example.model.SortOrder
import com.example.ui.components.BadgePill
import com.example.ui.components.CompactHorizontalGameCard
import com.example.ui.components.FeaturedHeroCard
import com.example.ui.components.GameGridCard
import com.example.ui.components.HardwareRigBanner
import com.example.ui.theme.CloudBg
import com.example.ui.theme.CloudBorder
import com.example.ui.theme.CloudSurface
import com.example.ui.theme.CloudSurfaceHigh
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.RtxViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VkBlue
import com.example.viewmodel.CatalogUiState

@Composable
fun CatalogScreen(
    uiState: CatalogUiState,
    onSearchChange: (String) -> Unit,
    onSelectGenre: (GameGenre) -> Unit,
    onToggleOnly3D: () -> Unit,
    onSelectSort: (SortOrder) -> Unit,
    onGameClick: (GameItem) -> Unit,
    onPlayGame: (GameItem) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onRigBannerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 164.dp),
        modifier = modifier
            .fillMaxSize()
            .background(CloudBg)
            .testTag("catalog_grid"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Top Header Brand & Total Game Counter
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(VkBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CloudQueue,
                            contentDescription = null,
                            tint = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary
                        )
                        Text(
                            text = "${uiState.allGamesCount} игр • Мгновенный запуск",
                            style = MaterialTheme.typography.labelMedium,
                            color = CyberEmerald
                        )
                    }
                }

                BadgePill(
                    text = "${uiState.streamSettings.resolution} • ${uiState.streamSettings.targetFps} FPS",
                    background = CloudSurfaceHigh,
                    contentColor = CyberEmerald
                )
            }
        }

        // Search Field
        item(span = { GridItemSpan(maxLineSpan) }) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_input"),
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = TextSecondary)
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchChange("") },
                            modifier = Modifier.testTag("search_clear_btn")
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = stringResource(R.string.action_close),
                                tint = TextSecondary
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CloudSurface,
                    unfocusedContainerColor = CloudSurface,
                    focusedBorderColor = VkBlue,
                    unfocusedBorderColor = CloudBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )
        }

        // Featured Hero Pager (shown when search is empty)
        if (uiState.searchQuery.isBlank() && uiState.featuredGames.isNotEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                val pagerState = rememberPagerState(pageCount = { uiState.featuredGames.size })
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    HorizontalPager(
                        state = pagerState,
                        pageSpacing = 12.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val game = uiState.featuredGames[page]
                        FeaturedHeroCard(
                            game = game,
                            onPlayClick = { onPlayGame(game) },
                            onCardClick = { onGameClick(game) }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(uiState.featuredGames.size) { index ->
                            val selected = pagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .size(width = if (selected) 20.dp else 6.dp, height = 6.dp)
                                    .clip(CircleShape)
                                    .background(if (selected) VkBlue else CloudBorder)
                            )
                        }
                    }
                }
            }
        }

        // Hardware Rig Summary Banner
        item(span = { GridItemSpan(maxLineSpan) }) {
            HardwareRigBanner(
                latencyMs = uiState.latencyCheck.measuredMs,
                onClick = onRigBannerClick
            )
        }

        // Recently Played Horizontal Strip
        if (uiState.recentGames.isNotEmpty() && uiState.searchQuery.isBlank()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Продолжить в браузере",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(uiState.recentGames, key = { "recent_${it.id}" }) { game ->
                            CompactHorizontalGameCard(
                                game = game,
                                onClick = { onPlayGame(game) }
                            )
                        }
                    }
                }
            }
        }

        // Genre Chips + 3D Filter + Sort Header
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = uiState.only3DFilter,
                            onClick = onToggleOnly3D,
                            label = { Text("Только 3D (${uiState.popular3DCount})") },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.ViewInAr,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RtxViolet,
                                selectedLabelColor = TextPrimary,
                                selectedLeadingIconColor = TextPrimary,
                                containerColor = CloudSurface,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = uiState.only3DFilter,
                                borderColor = CloudBorder
                            ),
                            modifier = Modifier.testTag("chip_only_3d")
                        )
                    }

                    items(GameGenre.entries) { genre ->
                        val selected = uiState.selectedGenre == genre
                        FilterChip(
                            selected = selected,
                            onClick = { onSelectGenre(genre) },
                            label = {
                                Text(
                                    if (genre == GameGenre.ALL) "Все (${uiState.allGamesCount})"
                                    else genre.labelRu
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = VkBlue,
                                selectedLabelColor = TextPrimary,
                                containerColor = CloudSurface,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selected,
                                borderColor = CloudBorder
                            ),
                            modifier = Modifier.testTag("chip_genre_${genre.name.lowercase()}")
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Найдено игр: ${uiState.filteredGames.size}",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )

                    Box {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = CloudSurface,
                            border = BorderStroke(1.dp, CloudBorder),
                            modifier = Modifier
                                .testTag("sort_dropdown_btn")
                                .clickable { sortMenuExpanded = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Sort,
                                    contentDescription = null,
                                    tint = VkBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = uiState.sortOrder.labelRu,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = TextPrimary
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = sortMenuExpanded,
                            onDismissRequest = { sortMenuExpanded = false },
                            containerColor = CloudSurfaceHigh
                        ) {
                            SortOrder.entries.forEach { order ->
                                DropdownMenuItem(
                                    text = { Text(order.labelRu, color = TextPrimary) },
                                    onClick = {
                                        onSelectSort(order)
                                        sortMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Empty Search State
        if (uiState.filteredGames.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    color = CloudSurface,
                    border = BorderStroke(1.dp, CloudBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            tint = VkBlue,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "Ничего не найдено",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Text(
                            text = "Попробуйте изменить запрос или сбросить фильтр жанров.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }
        } else {
            // 168-game Catalog Grid
            items(uiState.filteredGames, key = { it.id }) { game ->
                GameGridCard(
                    game = game,
                    isFavorite = uiState.favoriteIds.contains(game.id),
                    onCardClick = { onGameClick(game) },
                    onPlayClick = { onPlayGame(game) },
                    onFavoriteClick = { onToggleFavorite(game.id) }
                )
            }
        }
    }
}
