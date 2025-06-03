package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.Rocket
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.luaj.vm2.Globals
import org.luaj.vm2.lib.jse.JsePlatform
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock
import kotlin.test.assertEquals

class LuaLocationTest {

    fun run(script: String): Any {
        val globals: Globals = JsePlatform.standardGlobals()
        globals.set("location", LuaLocations().table)

        val newScript = """
            local loc = location.new(1, 2, 3, "world", 0, 0)
            $script
        """.trimIndent()

        val result = globals.load(newScript, "test.lua").call()

        return result
    }

    @Test
    fun `new should create a new LuaLocation instance`() {
        val result = run("return loc.x")

        assertEquals(1.0, result.toString().toDouble())
    }
}