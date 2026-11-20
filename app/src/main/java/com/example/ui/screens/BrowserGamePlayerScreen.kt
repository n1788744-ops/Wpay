package com.example.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.R
import com.example.model.GameItem
import com.example.model.StreamSettings
import com.example.ui.components.BadgePill
import com.example.ui.theme.CloudBorder
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VkBlue

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrowserGamePlayerScreen(
    game: GameItem,
    streamSettings: StreamSettings,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onExit() }

    var showOverlayDetails by remember { mutableStateOf(false) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    val targetUrl = remember(game.id, streamSettings) {
        if (streamSettings.useCustomStreamUrl &&
            (streamSettings.customStreamBaseUrl.startsWith("https://") ||
                streamSettings.customStreamBaseUrl.startsWith("http://"))
        ) {
            val sep = if (streamSettings.customStreamBaseUrl.contains("?")) "&" else "?"
            "${streamSettings.customStreamBaseUrl}${sep}game=${Uri.encode(game.id)}&title=${Uri.encode(game.title)}"
        } else {
            "file:///android_asset/games/${game.engineType.assetFile}" +
                "?title=${Uri.encode(game.title)}" +
                "&accent=${Uri.encode(game.accentHex)}" +
                "&fps=${streamSettings.targetFps}" +
                "&res=${Uri.encode(streamSettings.resolution)}"
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF05070E))
            .testTag("browser_player_screen")
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.mediaPlaybackRequiresUserGesture = false
                    settings.cacheMode = WebSettings.LOAD_DEFAULT
                    settings.useWideViewPort = true
                    settings.loadWithOverviewMode = true
                    webChromeClient = WebChromeClient()
                    webViewClient = WebViewClient()
                    loadUrl(targetUrl)
                    webViewRef = this
                }
            },
            update = { view ->
                webViewRef = view
            }
        )

        // Compact floating Cloud Stream Overlay Bar at top
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xCC0B1020),
                border = BorderStroke(1.dp, VkBlue.copy(alpha = 0.55f))
            ) {
                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onExit,
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("player_exit_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.action_back),
                                    tint = TextPrimary
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = game.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${streamSettings.resolution} • ${streamSettings.targetFps} FPS • ${streamSettings.codec}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CyberEmerald
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { webViewRef?.reload() },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("player_reload_btn")
                            ) {
                                Icon(Icons.Filled.Refresh, contentDescription = "Перезапуск", tint = TextPrimary)
                            }
                            IconButton(
                                onClick = { showOverlayDetails = !showOverlayDetails },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("player_hud_toggle_btn")
                            ) {
                                Icon(
                                    imageVector = if (showOverlayDetails) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                    contentDescription = "Детали потока",
                                    tint = TextPrimary
                                )
                            }
                        }
                    }

                    AnimatedVisibility(visible = showOverlayDetails) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BadgePill("RTX 4070 SUPER 11999 Mb", VkBlue.copy(alpha = 0.25f), TextPrimary)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Ryzen 9 5900X • 32 Gb • Битрейт ${streamSettings.bitrateMbps} Мбит/с",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
