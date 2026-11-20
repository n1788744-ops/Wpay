package com.example.model

import androidx.compose.ui.graphics.Color

enum class GameGenre(val labelRu: String) {
    ALL("Все"),
    ACTION("Экшен"),
    SHOOTER("Шутеры"),
    RACING("Гонки 3D"),
    RPG("RPG"),
    STRATEGY("Стратегии"),
    ADVENTURE("Приключения"),
    SPORT("Спорт"),
    HORROR("Хоррор"),
    INDIE("Аркады")
}

enum class EngineType(val assetFile: String, val engineLabelRu: String) {
    RACING_3D("cyber_racer_3d.html", "3D Драйв"),
    SPACE_3D("starfighter_3d.html", "3D Космос"),
    FPS_3D("dungeon_fps_3d.html", "3D Шутер"),
    ARENA_3D("tactical_arena_3d.html", "3D Арена"),
    RUNNER_3D("neon_parkour_3d.html", "3D Паркур")
}

enum class CoverMotif {
    CYBER_CITY,
    NEON_HIGHWAY,
    STAR_NEBULA,
    FANTASY_CITADEL,
    TACTICAL_GRID,
    HORROR_ABYSS,
    ARENA_STADIUM,
    MECH_CORE
}

enum class SortOrder(val labelRu: String) {
    POPULAR("Популярные"),
    RATING("Высокий рейтинг"),
    LOWEST_PING("Мин. задержка"),
    NEWEST("Новинки")
}

data class GameItem(
    val id: String,
    val title: String,
    val genre: GameGenre,
    val releaseYear: Int,
    val rating: Float,
    val playersOnline: Int,
    val isPopular3D: Boolean,
    val isFeatured: Boolean,
    val engineType: EngineType,
    val motif: CoverMotif,
    val accentHex: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val targetFps: Int,
    val maxResolution: String,
    val rtxSupported: Boolean,
    val estimatedPingMs: Int,
    val description: String,
    val tags: List<String>
)

data class ServerNode(
    val id: String,
    val name: String,
    val region: String,
    val pingHost: String,
    val gpuSummary: String,
    val loadPercent: Int
)

data class StreamSettings(
    val resolution: String = "1440p",
    val targetFps: Int = 120,
    val bitrateMbps: Int = 55,
    val codec: String = "AV1",
    val lowLatencyMode: Boolean = true,
    val selectedNodeId: String = "ru-msk-01",
    val customStreamBaseUrl: String = "",
    val useCustomStreamUrl: Boolean = false
)

data class LatencyCheckResult(
    val isChecking: Boolean = false,
    val measuredMs: Int? = null,
    val statusText: String = "Готов к замеру",
    val isOfflineFallback: Boolean = false
)

data class RigHardwareSpec(
    val cpuName: String = "AMD Ryzen 9 5900X 12-Core Processor",
    val cpuDetails: String = "12 ядер / 24 потока • до 4.8 ГГц",
    val gpuName: String = "NVIDIA GeForce RTX 4070 SUPER 11999 Mb",
    val gpuDetails: String = "12 ГБ GDDR6X • DLSS 3.5 • NVENC AV1",
    val ramName: String = "32 Gb DDR4-3600",
    val ramDetails: String = "32 Gb для плавного воспроизведения контента"
)
