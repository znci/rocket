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
import dev.znci.rocket.scripting.globals.*
import dev.znci.rocket.scripting.globals.tables.LuaCommands
import dev.znci.rocket.scripting.globals.tables.LuaEvents
import dev.znci.rocket.scripting.globals.tables.LuaHTTPClient
import dev.znci.rocket.scripting.globals.tables.LuaLocations
import dev.znci.rocket.scripting.globals.tables.LuaPlayers
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

    var enabledGlobals: MutableList<RocketValueBase> = mutableListOf()
    var enabledGlobalValues: MutableList<RocketProperty> = mutableListOf()

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
            applyGlobals(globals)
            val scriptResult = globals.load(text, "script", globals)

            scriptResult.call()
        } catch (error: LuaError) {
            return error.message
        }

        return ""
    }

    private fun getGlobalByTableName(valueName: String): RocketValueBase? {
        return enabledGlobals.find { it -> it.valueName == valueName }
    }

    fun registerGlobal(global: RocketValueBase) {
        if (getGlobalByTableName(global.valueName) != null) {
            throw RocketError("A global of the same table name ('${global.valueName}') is already registered.")
        }

        enabledGlobals.add(global)
    }

    fun unregisterGlobal(global: RocketValueBase) {
        if (getGlobalByTableName(global.valueName) == null) {
            throw RocketError("A global with the table name ('${global.valueName}') is not registered and cannot be unregistered.")
        }

        enabledGlobals.remove(global)
    }

    fun applyGlobals(table: LuaTable) {
        enabledGlobals.forEach { it ->
            when (it) {
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