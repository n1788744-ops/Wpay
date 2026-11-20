package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GameCatalogProvider
import com.example.data.PreferencesRepository
import com.example.model.GameGenre
import com.example.model.GameItem
import com.example.model.LatencyCheckResult
import com.example.model.RigHardwareSpec
import com.example.model.ServerNode
import com.example.model.SortOrder
import com.example.model.StreamSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

data class CatalogUiState(
    val allGamesCount: Int = 0,
    val popular3DCount: Int = 0,
    val featuredGames: List<GameItem> = emptyList(),
    val filteredGames: List<GameItem> = emptyList(),
    val favoriteGames: List<GameItem> = emptyList(),
    val recentGames: List<GameItem> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
    val searchQuery: String = "",
    val selectedGenre: GameGenre = GameGenre.ALL,
    val only3DFilter: Boolean = false,
    val sortOrder: SortOrder = SortOrder.POPULAR,
    val streamSettings: StreamSettings = StreamSettings(),
    val rigHardware: RigHardwareSpec = RigHardwareSpec(),
    val serverNodes: List<ServerNode> = GameCatalogProvider.serverNodes,
    val latencyCheck: LatencyCheckResult = LatencyCheckResult()
)

class CloudPlayViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsRepo = PreferencesRepository(application.applicationContext)
    private val allGames = GameCatalogProvider.allGames
    private val gamesById = allGames.associateBy { it.id }

    private val searchQuery = MutableStateFlow("")
    private val selectedGenre = MutableStateFlow(GameGenre.ALL)
    private val only3DFilter = MutableStateFlow(false)
    private val sortOrder = MutableStateFlow(SortOrder.POPULAR)
    private val latencyCheck = MutableStateFlow(LatencyCheckResult())

    private data class FilterState(
        val query: String,
        val genre: GameGenre,
        val only3D: Boolean,
        val sort: SortOrder
    )

    private val filterFlow = combine(
        searchQuery,
        selectedGenre,
        only3DFilter,
        sortOrder
    ) { q, g, d3, s -> FilterState(q, g, d3, s) }

    val uiState: StateFlow<CatalogUiState> = combine(
        filterFlow,
        prefsRepo.favoriteIdsFlow,
        prefsRepo.recentIdsFlow,
        prefsRepo.streamSettingsFlow,
        latencyCheck
    ) { filter, favIds, recentIds, settings, latency ->
        val trimmedQuery = filter.query.trim().lowercase()
        val filtered = allGames
            .filter { game ->
                val matchesGenre = filter.genre == GameGenre.ALL || game.genre == filter.genre
                val matches3D = !filter.only3D || game.isPopular3D
                val matchesQuery = trimmedQuery.isEmpty() ||
                    game.title.lowercase().contains(trimmedQuery) ||
                    game.genre.labelRu.lowercase().contains(trimmedQuery) ||
                    game.tags.any { it.lowercase().contains(trimmedQuery) }
                matchesGenre && matches3D && matchesQuery
            }
            .let { list ->
                when (filter.sort) {
                    SortOrder.POPULAR -> list.sortedByDescending { it.playersOnline }
                    SortOrder.RATING -> list.sortedByDescending { it.rating }
                    SortOrder.LOWEST_PING -> list.sortedBy { it.estimatedPingMs }
                    SortOrder.NEWEST -> list.sortedByDescending { it.releaseYear }
                }
            }

        CatalogUiState(
            allGamesCount = allGames.size,
            popular3DCount = allGames.count { it.isPopular3D },
            featuredGames = allGames.filter { it.isFeatured },
            filteredGames = filtered,
            favoriteGames = favIds.mapNotNull { gamesById[it] },
            recentGames = recentIds.mapNotNull { gamesById[it] },
            favoriteIds = favIds,
            searchQuery = filter.query,
            selectedGenre = filter.genre,
            only3DFilter = filter.only3D,
            sortOrder = filter.sort,
            streamSettings = settings,
            latencyCheck = latency
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CatalogUiState(
            allGamesCount = allGames.size,
            popular3DCount = allGames.count { it.isPopular3D },
            featuredGames = allGames.filter { it.isFeatured },
            filteredGames = allGames
        )
    )

    fun getGameById(id: String): GameItem? = gamesById[id]

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun selectGenre(genre: GameGenre) {
        selectedGenre.value = genre
    }

    fun toggleOnly3D() {
        only3DFilter.value = !only3DFilter.value
    }

    fun setSortOrder(order: SortOrder) {
        sortOrder.value = order
    }

    fun toggleFavorite(gameId: String) {
        viewModelScope.launch {
            prefsRepo.toggleFavorite(gameId)
        }
    }

    fun recordLaunch(gameId: String) {
        viewModelScope.launch {
            prefsRepo.recordRecentLaunch(gameId)
        }
    }

    fun updateResolution(res: String) {
        viewModelScope.launch {
            prefsRepo.updateStreamSettings { it.copy(resolution = res) }
        }
    }

    fun updateTargetFps(fps: Int) {
        viewModelScope.launch {
            prefsRepo.updateStreamSettings { it.copy(targetFps = fps) }
        }
    }

    fun updateBitrate(mbps: Int) {
        viewModelScope.launch {
            prefsRepo.updateStreamSettings { it.copy(bitrateMbps = mbps) }
        }
    }

    fun updateCodec(codec: String) {
        viewModelScope.launch {
            prefsRepo.updateStreamSettings { it.copy(codec = codec) }
        }
    }

    fun toggleLowLatencyMode() {
        viewModelScope.launch {
            prefsRepo.updateStreamSettings { it.copy(lowLatencyMode = !it.lowLatencyMode) }
        }
    }

    fun selectServerNode(nodeId: String) {
        viewModelScope.launch {
            prefsRepo.updateStreamSettings { it.copy(selectedNodeId = nodeId) }
        }
    }

    fun updateCustomStreamConfig(url: String, enabled: Boolean) {
        viewModelScope.launch {
            prefsRepo.updateStreamSettings {
                it.copy(customStreamBaseUrl = url.trim(), useCustomStreamUrl = enabled)
            }
        }
    }

    fun checkNodeLatency() {
        val nodeId = uiState.value.streamSettings.selectedNodeId
        val node = GameCatalogProvider.serverNodes.find { it.id == nodeId }
            ?: GameCatalogProvider.serverNodes.first()

        viewModelScope.launch {
            latencyCheck.value = LatencyCheckResult(
                isChecking = true,
                measuredMs = null,
                statusText = "Измеряем RTT до ${node.name}…"
            )
            val result = withContext(Dispatchers.IO) {
                try {
                    val startNs = System.nanoTime()
                    Socket().use { socket ->
                        socket.connect(InetSocketAddress(node.pingHost, 443), 2500)
                    }
                    val elapsedMs = ((System.nanoTime() - startNs) / 1_000_000L).toInt().coerceAtLeast(1)
                    LatencyCheckResult(
                        isChecking = false,
                        measuredMs = elapsedMs,
                        statusText = "Узел доступен • $elapsedMs мс (TCP RTT)",
                        isOfflineFallback = false
                    )
                } catch (_: Exception) {
                    LatencyCheckResult(
                        isChecking = false,
                        measuredMs = null,
                        statusText = "Нет сети — активен локальный WebGL 3D движок (0 мс)",
                        isOfflineFallback = true
                    )
                }
            }
            latencyCheck.value = result
        }
    }
}
