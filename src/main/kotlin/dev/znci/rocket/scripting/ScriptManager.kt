package dev.znci.rocket.scripting

import dev.znci.rocket.scripting.functions.LuaPlayers
import org.bukkit.event.Event
import java.io.File
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.lib.jse.JsePlatform

object ScriptManager {
    var scriptsFolder: File = File("")
    val globals: Globals = JsePlatform.standardGlobals()

    var usedEvents: MutableMap<Event, LuaFunction> = mutableMapOf()

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

    fun runScript(text: String): String? {
        try {
            globals.set("players", LuaPlayers())
            val scriptResult = globals.load(text, "script", globals)

            scriptResult.call()
        } catch (error: LuaError) {
            return error.message
        }

        return ""
    }
}