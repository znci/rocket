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

import dev.znci.rocket.scripting.classes.Command
import dev.znci.rocket.scripting.functions.*
import org.bukkit.event.Event
import java.io.File
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.JsePlatform
import java.util.ArrayList

object ScriptManager {
    var scriptsFolder: File = File("")
    val globals: Globals = JsePlatform.standardGlobals()

    val usedEvents = mutableMapOf<Class<out Event>, LuaValue>()
    val enabledCommands = mutableMapOf<String, Command>()

    fun setFolder(folder: File) {
        scriptsFolder = folder
    }

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
            val scriptResult = globals.load(text, "script", globals)

            scriptResult.call()
        } catch (error: LuaError) {
            return error.message
        }

        return ""
    }
}