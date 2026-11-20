package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.model.GameItem
import com.example.ui.components.GameDetailSheet
import com.example.ui.screens.BrowserGamePlayerScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.Popular3DScreen
import com.example.ui.screens.ServerRigScreen
import com.example.ui.theme.CloudBg
import com.example.ui.theme.CloudPlayTheme
import com.example.ui.theme.CloudSurface
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VkBlue
import com.example.viewmodel.CloudPlayViewModel

private const val ROUTE_CATALOG = "catalog"
private const val ROUTE_POPULAR_3D = "popular_3d"
private const val ROUTE_LIBRARY = "library"
private const val ROUTE_RIG = "server_rig"
private const val ROUTE_PLAYER = "browser_player/{gameId}"

private data class TopNavItem(
    val route: String,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CloudPlayTheme {
                CloudPlayApp()
            }
        }
    }
}

@Composable
fun CloudPlayApp(viewModel: CloudPlayViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val isPlayerRoute = currentRoute?.startsWith("browser_player") == true
    val context = LocalContext.current

    var selectedDetailGame by remember { mutableStateOf<GameItem?>(null) }

    val navItems = remember {
        listOf(
            TopNavItem(ROUTE_CATALOG, R.string.nav_catalog, Icons.Filled.SportsEsports, Icons.Outlined.SportsEsports),
            TopNavItem(ROUTE_POPULAR_3D, R.string.nav_popular_3d, Icons.Filled.ViewInAr, Icons.Outlined.ViewInAr),
            TopNavItem(ROUTE_LIBRARY, R.string.nav_library, Icons.Filled.Bookmarks, Icons.Outlined.Bookmarks),
            TopNavItem(ROUTE_RIG, R.string.nav_rig, Icons.Filled.Dns, Icons.Outlined.Dns)
        )
    }

    val navigateTab: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    val launchGameInBrowser: (GameItem) -> Unit = { game ->
        selectedDetailGame = null
        viewModel.recordLaunch(game.id)
        navController.navigate("browser_player/${game.id}")
    }

    val openExternalBrowser: (GameItem) -> Unit = { game ->
        val base = uiState.streamSettings.customStreamBaseUrl
        if (base.startsWith("https://") || base.startsWith("http://")) {
            val sep = if (base.contains("?")) "&" else "?"
            val uri = Uri.parse("${base}${sep}game=${Uri.encode(game.id)}&title=${Uri.encode(game.title)}")
            selectedDetailGame = null
            viewModel.recordLaunch(game.id)
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudBg)
    ) {
        val useRail = maxWidth >= 600.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = CloudBg,
            contentWindowInsets = if (isPlayerRoute) WindowInsets(0, 0, 0, 0) else WindowInsets.safeDrawing,
            bottomBar = {
                if (!useRail && !isPlayerRoute) {
                    NavigationBar(
                        containerColor = CloudSurface,
                        contentColor = TextPrimary
                    ) {
                        navItems.forEach { item ->
                            val selected = currentRoute == item.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navigateTab(item.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = stringResource(item.labelRes)
                                    )
                                },
                                label = {
                                    Text(
                                        text = stringResource(item.labelRes),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = TextPrimary,
                                    selectedTextColor = CyberEmerald,
                                    indicatorColor = VkBlue,
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextSecondary
                                ),
                                modifier = Modifier.testTag("nav_${item.route}")
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (useRail && !isPlayerRoute) {
                    NavigationRail(
                        containerColor = CloudSurface,
                        contentColor = TextPrimary,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        navItems.forEach { item ->
                            val selected = currentRoute == item.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateTab(item.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = stringResource(item.labelRes)
                                    )
                                },
                                label = {
                                    Text(
                                        text = stringResource(item.labelRes),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = TextPrimary,
                                    selectedTextColor = CyberEmerald,
                                    indicatorColor = VkBlue,
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextSecondary
                                ),
                                modifier = Modifier.testTag("rail_${item.route}")
                            )
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = ROUTE_CATALOG,
                    modifier = Modifier.weight(1f)
                ) {
                    composable(ROUTE_CATALOG) {
                        CatalogScreen(
                            uiState = uiState,
                            onSearchChange = viewModel::updateSearchQuery,
                            onSelectGenre = viewModel::selectGenre,
                            onToggleOnly3D = viewModel::toggleOnly3D,
                            onSelectSort = viewModel::setSortOrder,
                            onGameClick = { selectedDetailGame = it },
                            onPlayGame = launchGameInBrowser,
                            onToggleFavorite = viewModel::toggleFavorite,
                            onRigBannerClick = { navigateTab(ROUTE_RIG) }
                        )
                    }

                    composable(ROUTE_POPULAR_3D) {
                        Popular3DScreen(
                            uiState = uiState,
                            onGameClick = { selectedDetailGame = it },
                            onPlayGame = launchGameInBrowser,
                            onToggleFavorite = viewModel::toggleFavorite
                        )
                    }

                    composable(ROUTE_LIBRARY) {
                        LibraryScreen(
                            uiState = uiState,
                            onGameClick = { selectedDetailGame = it },
                            onPlayGame = launchGameInBrowser,
                            onToggleFavorite = viewModel::toggleFavorite
                        )
                    }

                    composable(ROUTE_RIG) {
                        ServerRigScreen(
                            uiState = uiState,
                            onSelectNode = viewModel::selectServerNode,
                            onRunLatencyTest = viewModel::checkNodeLatency,
                            onSelectResolution = viewModel::updateResolution,
                            onSelectFps = viewModel::updateTargetFps,
                            onBitrateChange = viewModel::updateBitrate,
                            onSelectCodec = viewModel::updateCodec,
                            onToggleLowLatency = viewModel::toggleLowLatencyMode,
                            onSaveCustomStream = viewModel::updateCustomStreamConfig
                        )
                    }

                    composable(
                        route = ROUTE_PLAYER,
                        arguments = listOf(navArgument("gameId") { type = NavType.StringType })
                    ) { entry ->
                        val gameId = entry.arguments?.getString("gameId") ?: "game_1"
                        val game = viewModel.getGameById(gameId)
                            ?: uiState.featuredGames.first()
                        BrowserGamePlayerScreen(
                            game = game,
                            streamSettings = uiState.streamSettings,
                            onExit = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }

    selectedDetailGame?.let { game ->
        GameDetailSheet(
            game = game,
            isFavorite = uiState.favoriteIds.contains(game.id),
            streamSettings = uiState.streamSettings,
            onDismiss = { selectedDetailGame = null },
            onPlayInAppBrowser = { launchGameInBrowser(game) },
            onOpenExternalBrowser = { openExternalBrowser(game) },
            onToggleFavorite = { viewModel.toggleFavorite(game.id) }
        )
    }
}
