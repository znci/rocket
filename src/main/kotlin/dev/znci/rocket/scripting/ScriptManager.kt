/**
 * Copyright 2025 znci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.znci.rocket.scripting

import dev.znci.rocket.scripting.api.RocketError
import dev.znci.rocket.scripting.api.RocketLuaValue
import dev.znci.rocket.scripting.api.RocketProperty
import dev.znci.rocket.scripting.api.RocketTable
import dev.znci.rocket.scripting.api.RocketValueBase
import dev.znci.rocket.scripting.classes.Command
import org.bukkit.event.Event
import java.io.File
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.JsePlatform
import java.util.ArrayList

/**
 * The `ScriptManager` object is responsible for managing Lua scripts, global values, and event handling in the plugin.
 * It allows for the loading and running of scripts, registering and unregistering global values.
 */
object ScriptManager {
    /**
     * The global environment for Lua scripts.
     * This is used to load and execute Lua code with a standard Lua environment.
     */
    private val globals: Globals = JsePlatform.standardGlobals()

    /**
     * The folder where scripts are located.
     * This can be set to a custom folder to load Lua scripts from a specific directory.
     */
    var scriptsFolder: File = File("")

    /**
     * A map of events and their associated Lua handlers.
     * It stores the events triggered in the system and the corresponding Lua functions that handle them.
     */
    val usedEvents = mutableMapOf<Class<out Event>, LuaValue>()

    /**
     * A map of enabled commands by their names.
     * It associates command names with their respective command executors.
     */
    val enabledCommands = mutableMapOf<String, Command>()

    /**
     * A list of global values (properties and tables) that have been registered for Lua scripting.
     * These globals are made available to Lua scripts during their execution.
     */
    var enabledGlobals: MutableList<RocketValueBase> = mutableListOf()

    /**
     * Sets the folder where scripts are located.
     *
     * @param folder The folder containing the Lua scripts.
     */
    @Suppress("unused") // TODO: Will be used in the future when custom configuration folders are implemented
    fun setFolder(folder: File) {
        scriptsFolder = folder
    }

    /**
     * Loads all scripts from the `scriptsFolder` directory.
     * This method currently prints the content of the scripts, but is planned for future use when custom folder configurations are implemented.
     */
    @Suppress("unused") // TODO: Is this still required?
    fun loadScripts() {
        scriptsFolder.walkTopDown().forEach { file ->
            if (file.isFile && !file.startsWith("-")) {
                val content = file.readText()

                println(content)
            }
        }
    }

    /**
     * Retrieves a list of all scripts available in the `scriptsFolder` directory.
     * Optionally, can include or exclude disabled scripts based on their file name (files starting with "-").
     *
     * @param includeDisabled If `true`, disabled scripts (starting with '-') are included.
     * @return A list of script file paths relative to the plugin directory.
     */
    fun getAllScripts(includeDisabled: Boolean = true): List<String> {
        val list = ArrayList<String>()
        scriptsFolder.walkTopDown().forEach { file ->
            if (file.isFile) {
                if (includeDisabled && file.startsWith("-")) return@forEach
                list.add(file.path.removePrefix("plugins/rocket/scripts/"))
            }
        }
        return list
    }

    /**
     * Runs a Lua script provided as a string.
     * The script is executed within the global Lua environment, and any errors are caught and returned as a string message.
     *
     * @param text The Lua script content to execute.
     * @return An error message if execution fails, or an empty string if the script ran successfully.
     */
    fun runScript(text: String): String? {
        try {
            applyGlobals(globals)
            val scriptResult = globals.load(text, "script", globals)

            scriptResult.call()
        } catch (e: LuaError) {
            return e.message
        }

        return ""
    }

    /**
     * Retrieves a global value by its table name.
     *
     * @param valueName The name of the global value to retrieve.
     * @return The global value if found, or `null` if it is not registered.
     */
    private fun getGlobalByTableName(valueName: String): RocketValueBase? {
        return enabledGlobals.find { it -> it.valueName == valueName }
    }

    /**
     * Registers a global value, making it available for use in Lua.
     *
     * @param global The global value to register.
     * @throws RocketError If a global with the same table name is already registered.
     */
    fun registerGlobal(global: RocketValueBase) {
        if (getGlobalByTableName(global.valueName) != null) {
            throw RocketError("A global of the same table name ('${global.valueName}') is already registered.")
        }

        enabledGlobals.add(global)
    }

    /**
     * Unregisters a global value, making it unavailable for use in Lua.
     *
     * @param global The global value to unregister.
     * @throws RocketError If no global with the given table name is registered.
     */
    fun unregisterGlobal(global: RocketValueBase) {
        if (getGlobalByTableName(global.valueName) == null) {
            throw RocketError("A global with the table name ('${global.valueName}') is not registered and cannot be unregistered.")
        }

        enabledGlobals.remove(global)
    }

    /**
     * Applies the registered global values to a Lua table.
     * This makes all registered globals available to Lua scripts via the provided Lua table.
     *
     * @param table The Lua table to which the globals will be applied.
     */
    fun applyGlobals(table: LuaTable) {
        enabledGlobals.forEach { it ->
            when (it) {
//                is RocketEnum -> {
//                    try {
//                        val luaTable = it.convertToLuaTable()
//                        table.set(it.valueName, luaTable.table)
//                    } catch (e: Exception) {
//                        println("Error in RocketEnum case: ${e.message}")
//                        e.printStackTrace()
//                    }
//                }
                is RocketTable -> {
                    table.set(it.valueName, it.table)
                }
                is RocketProperty -> {
                    table.set(it.valueName, RocketLuaValue.valueOf(it.value))
                }
            }
        }
    }
}