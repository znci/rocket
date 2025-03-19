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
import dev.znci.rocket.scripting.api.RocketGlobal
import dev.znci.rocket.scripting.api.RocketGlobalValue
import dev.znci.rocket.scripting.classes.Command
import dev.znci.rocket.scripting.globals.*
import org.bukkit.event.Event
import java.io.File
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.JsePlatform
import java.util.ArrayList

object ScriptManager {
    private val globals: Globals = JsePlatform.standardGlobals()

    var scriptsFolder: File = File("")
    val usedEvents = mutableMapOf<Class<out Event>, LuaValue>()
    val enabledCommands = mutableMapOf<String, Command>()

    var enabledGlobals: MutableList<RocketGlobal> = mutableListOf()
    var enabledGlobalValues: MutableList<RocketGlobalValue> = mutableListOf()

    @Suppress("unused") // TODO: Will be used in the future when custom configuration folders are implemented
    fun setFolder(folder: File) {
        scriptsFolder = folder
    }

    @Suppress("unused") // TODO: Is this still required?
    fun loadScripts() {
        scriptsFolder.walkTopDown().forEach { file ->
            if (file.isFile && !file.startsWith("-")) {
                val content = file.readText()

                println(content)
            }
        }
    }

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

    fun runScript(text: String): String? {
        try {
            globals.set("players", LuaPlayers())
            globals.set("events", LuaEvents())
            globals.set("commands", LuaCommands())
            globals.set("http", LuaHTTPClient())
            globals.set("location", LuaLocations())

            val testInstance = Test();
            globals.set("testNext", testInstance.getTable())
//            globals.load(testInstance).call()

            applyGlobals(globals)
            val scriptResult = globals.load(text, "script", globals)

            scriptResult.call()
        } catch (error: LuaError) {
            return error.message
        }

        return ""
    }

    private fun getGlobalByTableName(tableName: String): RocketGlobal? {
        return enabledGlobals.find { it -> it.tableName == tableName }
    }

    fun registerGlobal(global: RocketGlobal) {
        if (getGlobalByTableName(global.tableName) != null) {
            throw RocketError("A global of the same table name ('${global.tableName}') is already registered.")
        }

        enabledGlobals.add(global)
    }

    fun unregisterGlobal(global: RocketGlobal) {
        if (getGlobalByTableName(global.tableName) == null) {
            throw RocketError("A global with the table name ('${global.tableName}') is not registered and cannot be unregistered.")
        }

        enabledGlobals.remove(global)
    }

    fun registerGlobalValue(globalValue: RocketGlobalValue) {
        if (getGlobalByTableName(globalValue.valueName) != null) {
            throw RocketError("A global value of the same table name ('${globalValue.valueName}') is already registered.")
        }

        enabledGlobalValues.add(globalValue)
    }

    fun unregisterGlobalValue(globalValue: RocketGlobalValue) {
        if (getGlobalByTableName(globalValue.valueName) != null) {
            throw RocketError("A global with the table name ('${globalValue.valueName}') is not registered and cannot be unregistered.")
        }

        enabledGlobalValues.remove(globalValue)
    }

    fun applyGlobals(table: LuaTable) {
        enabledGlobals.forEach { it ->
            table.set(it.tableName, it.table)
        }
        enabledGlobalValues.forEach { it ->
            table.set(it.valueName, it.table)
        }
    }
}