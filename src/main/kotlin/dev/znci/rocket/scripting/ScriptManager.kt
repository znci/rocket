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
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.lib.jse.JsePlatform
import java.util.ArrayList

object ScriptManager {
    private val globals: Globals = JsePlatform.standardGlobals()

    val loadedScriptFiles = mutableMapOf<String, MutableList<LuaFunction>>()
    
    val usedEvents = mutableMapOf<Class<out Event>, MutableList<LuaFunction>>()
    val eventScript = mutableMapOf<LuaFunction, Class<out Event>>()
    
    val enabledCommands = mutableMapOf<String, Command>()

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
                list.add(file.path.removePrefix(if (file.path.startsWith("plugins/rocket/scripts/")) "plugins/rocket/scripts/" else "plugins\\rocket\\scripts\\"))
            }
        }
        return list
    }

    fun loadAll(): List<String?> {
        val results = mutableListOf<String?>()
        getAllScripts(false).forEach { script ->
            results.add(loadScript(File("plugins/rocket/scripts/", script)))
        }
        return results
    }

    fun loadScript(scriptFile: File): String? {
        println("Reloading from file: '${scriptFile.absolutePath}'")
        if (loadedScriptFiles[scriptFile.absolutePath] != null) {
            disableFile(scriptFile)
        }
        val result = runScript(scriptFile)
        return result
    }

    fun disableFile(scriptFile: File): String {
        println("Unloading functions from file: '${scriptFile.absolutePath}'")
        val functions = loadedScriptFiles[scriptFile.absolutePath]!!
        for (function in functions) {
            val eventClass = eventScript[function]!!
            for (eventCallback in usedEvents[eventClass]?:continue) {
                if (eventCallback == function) usedEvents.remove(eventClass)
            }
            eventScript.remove(function)
        }
        return ""
    }

    fun runScript(scriptFile: File): String? {

        val content = scriptFile.readText()

        try {
            globals.set("players", LuaPlayers())
            globals.set("events", LuaEvents())
            globals.set("commands", LuaCommands())
            globals.set("http", LuaHTTPClient())
            globals.set("location", LuaLocations())
            val scriptResult = globals.load(content, "::${scriptFile.absolutePath}::", globals)

            scriptResult.call()
        } catch (error: LuaError) {
            return error.message
        }

        return ""
    }
}