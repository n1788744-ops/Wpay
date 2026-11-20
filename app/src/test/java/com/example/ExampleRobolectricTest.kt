package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.EngineType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun readStringAndBrowserGameAssets() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("CloudPlay 3D", appName)

        val assets = context.assets.list("games")?.toSet() ?: emptySet()
        EngineType.entries.forEach { engine ->
            assertTrue("Asset missing: ${engine.assetFile}", assets.contains(engine.assetFile))
        }
    }
}
