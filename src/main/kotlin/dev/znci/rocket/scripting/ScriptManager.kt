package dev.znci.rocket.scripting

import dev.znci.rocket.scripting.classes.Command
import dev.znci.rocket.scripting.functions.LuaCommands
import dev.znci.rocket.scripting.functions.LuaEvents
import dev.znci.rocket.scripting.functions.LuaHTTPClient
import dev.znci.rocket.scripting.functions.LuaPlayers
import org.bukkit.entity.Player
import org.bukkit.event.Event
import java.io.File
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.JsePlatform

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

    fun runScript(text: String): String? {
        try {
            globals.set("players", LuaPlayers())
            globals.set("events", LuaEvents())
            globals.set("commands", LuaCommands())
            globals.set("http", LuaHTTPClient())
            val scriptResult = globals.load(text, "script", globals)

            scriptResult.call()
        } catch (error: LuaError) {
            return error.message
        }

        return ""
    }
}