package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.StreamSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.cloudPlayDataStore: DataStore<Preferences> by preferencesDataStore(name = "cloudplay_prefs")

class PreferencesRepository(private val context: Context) {

    private object Keys {
        val FAVORITE_IDS = stringSetPreferencesKey("favorite_game_ids")
        val RECENT_IDS = stringPreferencesKey("recent_game_ids_csv")
        val RESOLUTION = stringPreferencesKey("stream_resolution")
        val TARGET_FPS = intPreferencesKey("stream_fps")
        val BITRATE_MBPS = intPreferencesKey("stream_bitrate_mbps")
        val CODEC = stringPreferencesKey("stream_codec")
        val LOW_LATENCY = booleanPreferencesKey("stream_low_latency")
        val SELECTED_NODE = stringPreferencesKey("selected_node_id")
        val CUSTOM_STREAM_URL = stringPreferencesKey("custom_stream_base_url")
        val USE_CUSTOM_STREAM_URL = booleanPreferencesKey("use_custom_stream_url")
    }

    val favoriteIdsFlow: Flow<Set<String>> = context.cloudPlayDataStore.data.map { prefs ->
        prefs[Keys.FAVORITE_IDS] ?: setOf("game_1", "game_2", "game_3")
    }

    val recentIdsFlow: Flow<List<String>> = context.cloudPlayDataStore.data.map { prefs ->
        val raw = prefs[Keys.RECENT_IDS] ?: ""
        if (raw.isBlank()) emptyList() else raw.split(",").filter { it.isNotBlank() }
    }

    val streamSettingsFlow: Flow<StreamSettings> = context.cloudPlayDataStore.data.map { prefs ->
        StreamSettings(
            resolution = prefs[Keys.RESOLUTION] ?: "1440p",
            targetFps = prefs[Keys.TARGET_FPS] ?: 120,
            bitrateMbps = prefs[Keys.BITRATE_MBPS] ?: 55,
            codec = prefs[Keys.CODEC] ?: "AV1",
            lowLatencyMode = prefs[Keys.LOW_LATENCY] ?: true,
            selectedNodeId = prefs[Keys.SELECTED_NODE] ?: "ru-msk-01",
            customStreamBaseUrl = prefs[Keys.CUSTOM_STREAM_URL] ?: "",
            useCustomStreamUrl = prefs[Keys.USE_CUSTOM_STREAM_URL] ?: false
        )
    }

    suspend fun toggleFavorite(gameId: String) {
        context.cloudPlayDataStore.edit { prefs ->
            val current = prefs[Keys.FAVORITE_IDS]?.toMutableSet() ?: mutableSetOf("game_1", "game_2", "game_3")
            if (current.contains(gameId)) current.remove(gameId) else current.add(gameId)
            prefs[Keys.FAVORITE_IDS] = current
        }
    }

    suspend fun recordRecentLaunch(gameId: String) {
        context.cloudPlayDataStore.edit { prefs ->
            val current = (prefs[Keys.RECENT_IDS] ?: "")
                .split(",")
                .filter { it.isNotBlank() && it != gameId }
                .toMutableList()
            current.add(0, gameId)
            prefs[Keys.RECENT_IDS] = current.take(24).joinToString(",")
        }
    }

    suspend fun updateStreamSettings(transform: (StreamSettings) -> StreamSettings) {
        context.cloudPlayDataStore.edit { prefs ->
            val current = StreamSettings(
                resolution = prefs[Keys.RESOLUTION] ?: "1440p",
                targetFps = prefs[Keys.TARGET_FPS] ?: 120,
                bitrateMbps = prefs[Keys.BITRATE_MBPS] ?: 55,
                codec = prefs[Keys.CODEC] ?: "AV1",
                lowLatencyMode = prefs[Keys.LOW_LATENCY] ?: true,
                selectedNodeId = prefs[Keys.SELECTED_NODE] ?: "ru-msk-01",
                customStreamBaseUrl = prefs[Keys.CUSTOM_STREAM_URL] ?: "",
                useCustomStreamUrl = prefs[Keys.USE_CUSTOM_STREAM_URL] ?: false
            )
            val updated = transform(current)
            prefs[Keys.RESOLUTION] = updated.resolution
            prefs[Keys.TARGET_FPS] = updated.targetFps
            prefs[Keys.BITRATE_MBPS] = updated.bitrateMbps
            prefs[Keys.CODEC] = updated.codec
            prefs[Keys.LOW_LATENCY] = updated.lowLatencyMode
            prefs[Keys.SELECTED_NODE] = updated.selectedNodeId
            prefs[Keys.CUSTOM_STREAM_URL] = updated.customStreamBaseUrl
            prefs[Keys.USE_CUSTOM_STREAM_URL] = updated.useCustomStreamUrl
        }
    }
}
