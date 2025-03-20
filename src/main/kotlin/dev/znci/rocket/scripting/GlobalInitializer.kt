package dev.znci.rocket.scripting

import dev.znci.rocket.scripting.globals.tables.LuaPlayers
import dev.znci.rocket.scripting.globals.tables.SimpleTest
import dev.znci.rocket.scripting.globals.values.TestValue

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
    fun init(): Boolean {
        ScriptManager.registerGlobal(SimpleTest())
        ScriptManager.registerGlobal(TestValue())
        ScriptManager.registerGlobal(LuaPlayers())

        return true
    }
}