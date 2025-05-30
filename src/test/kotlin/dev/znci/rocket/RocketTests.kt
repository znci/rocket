package dev.znci.rocket

import org.junit.jupiter.api.BeforeEach
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock

class RocketTests {

    lateinit var mockServer: ServerMock
    lateinit var plugin: Rocket

    @BeforeEach
    fun setUp() {
        mockServer = MockBukkit.mock()
        plugin = MockBukkit.load(Rocket::class.java)
        mockServer.worlds.add(mockServer.addSimpleWorld("world"))
    }

}