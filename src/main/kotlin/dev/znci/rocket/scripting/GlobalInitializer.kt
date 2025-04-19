package dev.znci.rocket.scripting

import dev.znci.rocket.scripting.globals.tables.LuaCommands
import dev.znci.rocket.scripting.globals.tables.LuaHTTPClient
import dev.znci.rocket.scripting.globals.tables.LuaLocations
import dev.znci.rocket.scripting.globals.tables.LuaPlayers
import dev.znci.rocket.scripting.globals.tables.LuaServer
import dev.znci.rocket.scripting.globals.values.TestValue
import org.bukkit.plugin.java.JavaPlugin

/**
 * The `GlobalInitializer` object is responsible for initializing and registering global objects
 * with the `ScriptManager`. It ensures that necessary components are properly registered for use
 * for the server admins.
 */
object GlobalInitializer {
    /**
     * Initializes the global objects and registers them with the `ScriptManager`.
     *
     * It returns `true` upon successful registration of these objects.
     *
     * @return `true` if the global objects were successfully initialized and registered.
     */
    fun init(
        @Suppress("UNUSED")
        plugin: JavaPlugin
    ): Boolean {
        ScriptManager.registerGlobal(TestValue())
        ScriptManager.registerGlobal(LuaPlayers())
        ScriptManager.registerGlobal(LuaLocations())
        ScriptManager.registerGlobal(LuaHTTPClient())
        ScriptManager.registerGlobal(LuaCommands())
        ScriptManager.registerGlobal(LuaServer())
        //ScriptManager.registerGlobal(GamemodeEnum())

        return true
    }
}