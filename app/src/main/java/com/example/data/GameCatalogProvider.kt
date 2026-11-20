package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.model.CoverMotif
import com.example.model.EngineType
import com.example.model.GameGenre
import com.example.model.GameItem
import com.example.model.ServerNode

object GameCatalogProvider {

    val serverNodes: List<ServerNode> = listOf(
        ServerNode("ru-msk-01", "Москва • CloudNode #01", "Европа / РФ-Центр", "1.1.1.1", "RTX 4070 SUPER 11999 Mb", 34),
        ServerNode("ru-spb-02", "Санкт-Петербург • Edge #02", "Северо-Запад", "8.8.8.8", "RTX 4070 SUPER 11999 Mb", 28),
        ServerNode("eu-fra-03", "Франкфурт • Ultra Low-Ping", "Центр. Европа", "9.9.9.9", "RTX 4070 SUPER 11999 Mb", 41),
        ServerNode("ru-ekb-04", "Екатеринбург • Ural Rig", "Урал / Сибирь", "77.88.8.8", "RTX 4070 SUPER 11999 Mb", 22)
    )

    private data class PalettePair(val primary: Color, val secondary: Color, val hex: String)

    private val palettes = listOf(
        PalettePair(Color(0xFF0077FF), Color(0xFF00E5A3), "#0077FF"),
        PalettePair(Color(0xFF7B61FF), Color(0xFF00C2FF), "#7B61FF"),
        PalettePair(Color(0xFFFF3B6B), Color(0xFF7B61FF), "#FF3B6B"),
        PalettePair(Color(0xFF00B894), Color(0xFF0984E3), "#00B894"),
        PalettePair(Color(0xFFFF9F1C), Color(0xFFFF3B6B), "#FF9F1C"),
        PalettePair(Color(0xFF00D2FF), Color(0xFF3A7BD5), "#00D2FF"),
        PalettePair(Color(0xFFE056FD), Color(0xFF0077FF), "#E056FD"),
        PalettePair(Color(0xFF2ED573), Color(0xFF1E90FF), "#2ED573")
    )

    // 18 games per genre x 9 genres + 6 flagship specials = 168 games (> 150 games)
    private val flagshipEntries: List<Triple<String, GameGenre, String>> = listOf(
        Triple("Кибер-Горизонт 2099", GameGenre.ACTION, "Неоновый мегаполис 3D с трассировкой лучей RTX, динамическими боями и мгновенным запуском."),
        Triple("Ночной Форсаж: Токио 3D", GameGenre.RACING, "Высокоскоростной 3D-дрифт по дождливым трассам с физикой шин и поддержкой 120 FPS."),
        Triple("Цитадель Дракона: Эпоха 3D", GameGenre.RPG, "Масштабная фэнтези-RPG с открытым 3D-миром, осадами замков и тактической магией."),
        Triple("Сталь и Пепел: Штурм 3D", GameGenre.SHOOTER, "Тактический 3D-шутер от первого лица с разрушаемыми укрытиями и минимальной задержкой."),
        Triple("Астральный Рубеж: Эскадрилья", GameGenre.ACTION, "Космические 3D-сражения на орбите туманностей в разрешении до 4K без скачивания."),
        Triple("Квантовый Прорыв: Арена", GameGenre.STRATEGY, "Изометрическая 3D-стратегия реального времени с захватом энергоузлов и умным ИИ.")
    )

    private val genreTitles: Map<GameGenre, List<String>> = mapOf(
        GameGenre.ACTION to listOf(
            "Тень Самурая 3D", "Неоновый Клинок", "Протокол Титан", "Удар Валькирии", "Армада Зеро",
            "Грань Хаоса", "Гиперпрыжок 3D", "Охотник на Мехов", "Кровь и Молния", "Рейнджер Омега",
            "Синдикат Теней", "Реактивный Закат", "Хроники Киборга", "Плазма-Шторм", "Импульс Стали",
            "Вихрь Системы", "Рыцарь Будущего", "Легион Разлома"
        ),
        GameGenre.SHOOTER to listOf(
            "Периметр Альфа 3D", "Сектор-7: Зачистка", "Полигон Снайпера", "Операция «Вектор»", "Бастион 2077",
            "Шквал Огня 3D", "Контр-Удар: Арена", "Точка Невозврата", "Гвардия Космоса", "Тяжёлый Калибр",
            "Призрак Спецназа", "Красная Линия FPS", "Фронт Галактики", "Лазерный Рубеж", "Ультиматум 3D",
            "Боевой Дрон: Сеть", "Осада Бункера", "Кобальт-9"
        ),
        GameGenre.RACING to listOf(
            "Турбо Дрифт GT 3D", "Магистраль Неон", "Формула Облака", "Ралли Гран-При 3D", "Асфальт: Перегрузка",
            "Гиперкар Сеул", "Скорость Света 3D", "Картинг Турбо Лига", "Мото-Трек 2090", "Пустынный Рейд 3D",
            "Ночной Спринт PRO", "Апекс Трек", "Байкал Оффроуд 3D", "Кибер-Шоссе 88", "Драг-Рейсинг Мастер",
            "Гонки на Ховербайках", "Туринг Мастерс", "Нитро Легенда"
        ),
        GameGenre.RPG to listOf(
            "Клинок Вечности 3D", "Магия Северных Гор", "Тени Вальхаллы", "Хранитель Рун 3D", "Элизиум: Возрождение",
            "Подземелья Эфира", "Орден Феникса 3D", "Сказания Авалона", "Наследие Титанов", "Алхимик Сумрака",
            "Баллада Клинка", "Мир Кристаллов 3D", "Покоритель Драконов", "Трон Империи", "Тайны Гримуара",
            "Путь Паладина", "Аркана: Онлайн 3D", "Зов Древних"
        ),
        GameGenre.STRATEGY to listOf(
            "Империя Галактики 3D", "Железный Фронт", "Командир Базы", "Цивилизация Нова", "Тактика Осады",
            "Звёздный Флот: Тыл", "Операция Бастион", "Защита Сектора 3D", "Генералы Нефти", "Королевство Русь 3D",
            "Энергосеть: Битва", "Марс: Колонизация", "Синтез Империй", "Властелин Башен 3D", "Орбитальный Штаб",
            "Легионы Дюны", "Альянс Титанов", "Морской Доминион"
        ),
        GameGenre.ADVENTURE to listOf(
            "Остров Загадок 3D", "Тайна Затерянных Руин", "Экспедиция Арктика", "Горизонт Пиратов 3D", "Храм Солнца",
            "Небесный Капитан", "Сквозь Порталы 3D", "Долина Ветров", "Легенда Океана", "Археолог: Реликвия",
            "Джунгли Амазонки 3D", "Загадка Маяка", "Путешествие к Звёздам", "Эхо Пещер", "Одиссея Странника",
            "Ключ Времени 3D", "Город на Облаках", "Тропа Первопроходца"
        ),
        GameGenre.SPORT to listOf(
            "Кибер-Футбол 3D", "Арена Хоккея ПРО", "Стритбол Неон 3D", "Теннис Мастерс", "Ракетная Лига 3D",
            "Сноуборд Экстрим", "Бокс Чемпионов 3D", "Скейт-Парк Токио", "Бильярд Клаб 3D", "Волейбол на Пляже",
            "Кубок Чемпионов", "Гольф Империя 3D", "Лыжный Спуск", "Вело-Трек Тур", "Настольный Теннис ПРО",
            "Баскетбол: Данк 3D", "Паркур Стадион", "Триатлон Арена"
        ),
        GameGenre.HORROR to listOf(
            "Станция Мрак 3D", "Шёпот в Коридоре", "Лаборатория X-13", "Ночь в Санатории", "Бездна Океана 3D",
            "Туман Сайлента", "Проклятый Особняк", "Сигнал из Пустоты", "Заброшенный Бункер", "Тень Манекена 3D",
            "Лес Без Выхода", "Крипто-Хоррор 3D", "Холодный Сектор", "Эхо Катакомб", "Реактор: Заражение",
            "Тёмная Смена", "Секретный Архив", "Последний Фонарь"
        ),
        GameGenre.INDIE to listOf(
            "Неоновый Паркур 3D", "Кубический Бег", "Грави-Прыжок 3D", "Синтвейв Сёрфер", "Пиксель-Арена 3D",
            "Лазерный Лабиринт", "Башня Сфер 3D", "Головоломка Портал", "Спидран Профи", "Тетрис Неон 3D",
            "Энерго-Сфера", "Ритм Бластер 3D", "Космический Мяч", "Кибер-Кот: Прыжок", "Турбо-Куб",
            "Голограмма Пазл", "Векторный Бегун", "Хром-Аркада 3D"
        )
    )

    val allGames: List<GameItem> by lazy {
        val result = mutableListOf<GameItem>()
        var idx = 1

        // 1. Flagship featured titles
        flagshipEntries.forEachIndexed { fIdx, (title, genre, desc) ->
            val pal = palettes[fIdx % palettes.size]
            result += GameItem(
                id = "game_${idx++}",
                title = title,
                genre = genre,
                releaseYear = 2026,
                rating = 4.9f,
                playersOnline = 4820 - fIdx * 310,
                isPopular3D = true,
                isFeatured = true,
                engineType = engineForGenre(genre, fIdx),
                motif = motifForGenre(genre),
                accentHex = pal.hex,
                primaryColor = pal.primary,
                secondaryColor = pal.secondary,
                targetFps = 120,
                maxResolution = "4K UHD",
                rtxSupported = true,
                estimatedPingMs = 9 + (fIdx % 4),
                description = desc,
                tags = listOf("3D Графика", "RTX On", "Браузер 120 FPS", "Без скачивания")
            )
        }

        // 2. 162 catalog games across 9 genres (total = 168 games)
        genreTitles.forEach { (genre, titles) ->
            titles.forEachIndexed { tIdx, title ->
                val globalIndex = idx
                val pal = palettes[(globalIndex + genre.ordinal) % palettes.size]
                val is3D = (tIdx < 12) || genre == GameGenre.RACING || genre == GameGenre.SHOOTER || genre == GameGenre.ACTION
                val year = 2022 + (globalIndex % 5)
                val rating = (42 + ((globalIndex * 7) % 8)) / 10f
                val online = 340 + ((globalIndex * 197) % 3600)
                val fps = if (is3D && tIdx % 2 == 0) 120 else 60
                val res = if (tIdx % 3 == 0) "4K UHD" else "1440p"
                val ping = 8 + (globalIndex % 11)
                result += GameItem(
                    id = "game_${idx++}",
                    title = title,
                    genre = genre,
                    releaseYear = year,
                    rating = rating,
                    playersOnline = online,
                    isPopular3D = is3D,
                    isFeatured = false,
                    engineType = engineForGenre(genre, globalIndex),
                    motif = motifForGenre(genre),
                    accentHex = pal.hex,
                    primaryColor = pal.primary,
                    secondaryColor = pal.secondary,
                    targetFps = fps,
                    maxResolution = res,
                    rtxSupported = is3D,
                    estimatedPingMs = ping,
                    description = buildDescription(title, genre, fps, res),
                    tags = buildList {
                        add(if (is3D) "3D Движок" else "Облако HD")
                        add("$fps FPS")
                        if (is3D) add("RTX 4070 SUPER")
                        add("Мгновенный старт")
                    }
                )
            }
        }
        result
    }

    private fun engineForGenre(genre: GameGenre, seed: Int): EngineType = when (genre) {
        GameGenre.RACING, GameGenre.SPORT -> if (seed % 4 == 3) EngineType.RUNNER_3D else EngineType.RACING_3D
        GameGenre.SHOOTER, GameGenre.HORROR -> EngineType.FPS_3D
        GameGenre.ACTION -> if (seed % 2 == 0) EngineType.SPACE_3D else EngineType.FPS_3D
        GameGenre.RPG, GameGenre.STRATEGY -> EngineType.ARENA_3D
        GameGenre.ADVENTURE, GameGenre.INDIE -> if (seed % 2 == 0) EngineType.RUNNER_3D else EngineType.SPACE_3D
        GameGenre.ALL -> EngineType.SPACE_3D
    }

    private fun motifForGenre(genre: GameGenre): CoverMotif = when (genre) {
        GameGenre.ACTION -> CoverMotif.CYBER_CITY
        GameGenre.RACING -> CoverMotif.NEON_HIGHWAY
        GameGenre.SHOOTER -> CoverMotif.TACTICAL_GRID
        GameGenre.RPG -> CoverMotif.FANTASY_CITADEL
        GameGenre.STRATEGY -> CoverMotif.MECH_CORE
        GameGenre.ADVENTURE -> CoverMotif.STAR_NEBULA
        GameGenre.SPORT -> CoverMotif.ARENA_STADIUM
        GameGenre.HORROR -> CoverMotif.HORROR_ABYSS
        GameGenre.INDIE -> CoverMotif.CYBER_CITY
        GameGenre.ALL -> CoverMotif.CYBER_CITY
    }

    private fun buildDescription(title: String, genre: GameGenre, fps: Int, res: String): String {
        return "«$title» — популярная игра в жанре ${genre.labelRu}. Запускается мгновенно в браузерном облачном потоке до $res / $fps FPS на узле с RTX 4070 SUPER и 32 Gb RAM без установки на устройство."
    }
}
