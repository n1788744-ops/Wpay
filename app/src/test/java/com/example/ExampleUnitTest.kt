package com.example

import com.example.data.GameCatalogProvider
import com.example.model.EngineType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun catalogHasMoreThan150UniqueGames() {
        val games = GameCatalogProvider.allGames
        assertTrue("Expected > 150 games, got ${games.size}", games.size > 150)
        assertEquals(games.size, games.map { it.id }.toSet().size)
        assertEquals(games.size, games.map { it.title }.toSet().size)
        assertTrue(games.any { it.isPopular3D })
        EngineType.entries.forEach { engine ->
            assertTrue("Missing games for $engine", games.any { it.engineType == engine })
        }
    }
}
