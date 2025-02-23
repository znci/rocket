package dev.znci.rocket.commands

import dev.znci.rocket.scripting.ScriptManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class RocketCommand(private val plugin: JavaPlugin) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size != 2) {
            sender.sendMessage("§cUsage: /rocket <reload|disable> <scriptName>")
            return true
        }

        val action = args[0].lowercase()
        val scriptName = args[1]
        val scriptsFolder = File(plugin.dataFolder, "scripts")

        if (!scriptsFolder.exists() || !scriptsFolder.isDirectory) {
            sender.sendMessage("§cScripts folder not found!")
            return true
        }

        val scriptFile = File(scriptsFolder, scriptName)
        if (!scriptFile.exists()) {
            sender.sendMessage("§cScript '$scriptName' not found!")
            return true
        }

        when (action) {
            "reload" -> {
                val content = scriptFile.readText()
                sender.sendMessage("§aReloading script: $scriptName")

                val result = ScriptManager.runScript(content)

                if (result !== "") {
                    sender.sendMessage("§eError: §c$result")
                }
            }
            "disable" -> {
                sender.sendMessage("§cDisabling script: $scriptName")
            }
            else -> {
                sender.sendMessage("§cInvalid action! Use 'reload' or 'disable'.")
            }
        }
        return true
    }
}
