package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.Rocket
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock

/**
 * Tests for the `new` function in `LuaCommands` class.
 *
 * The `new` function is a Twine native function that creates and returns
 * a new instance of `LuaCommand`.
 */
class LuaCommandsTest {

    private lateinit var mockServer: ServerMock
    private lateinit var plugin: Rocket

    @BeforeEach
    fun setUp() {
        mockServer = MockBukkit.mock()
        plugin = MockBukkit.load(Rocket::class.java)
    }

    @Test
    fun `test new function returns new LuaCommand instance`() {
        // Arrange
        val luaCommands = LuaCommands()

        // Act
        val result = luaCommands.new()

        // Assert
        assertNotNull(result, "The new function should return a non-null LuaCommand instance.")
    }
}