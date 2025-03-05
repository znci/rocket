package dev.znci.rocket.commands

import dev.znci.rocket.i18n.LocaleManager
import dev.znci.rocket.scripting.ScriptManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class RocketCommand(private val plugin: JavaPlugin) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size != 2) {
            sender.sendMessage(LocaleManager.getMessageAsComponent("rocket_command.usage"))
            return true
        }

        val action = args[0].lowercase()
        val scriptName = args[1]
        val scriptsFolder = File(plugin.dataFolder, "scripts")

        if (!scriptsFolder.exists() || !scriptsFolder.isDirectory) {
            sender.sendMessage(LocaleManager.getMessageAsComponent("rocket_command.scripts_folder_not_found"))
            return true
        }

        when (action) {
            "reload" -> {
                if (scriptName.lowercase() == "config") {
                    plugin.reloadConfig()

                    val defaultLocale = plugin.config.getString("locale", "en_GB").toString()

                    LocaleManager.setLocale(defaultLocale)
                    LocaleManager.loadLanguages()

                    sender.sendMessage(LocaleManager.getMessageAsComponent("rocket_command.config_reloaded"))
                    return true
                }

                val scriptFile = File(scriptsFolder, scriptName)
                if (!scriptFile.exists()) {
                    sender.sendMessage(LocaleManager.getMessageAsComponent("rocket_command.script_not_found", scriptName))
                    return true
                }

                val content = scriptFile.readText()
                val result = ScriptManager.runScript(content)

                if (result !== "") {
                    sender.sendMessage(LocaleManager.getMessageAsComponent("generic_error", result ?: "Unknown error"))
                } else {
                    sender.sendMessage(
                        LocaleManager.getMessageAsComponent(
                            "rocket_command.script_reloaded",
                            scriptName
                        )
                    )
                }
            }
            "disable" -> {
                val scriptFile = File(scriptsFolder, scriptName)
                if (!scriptFile.exists()) {
                    sender.sendMessage(LocaleManager.getMessageAsComponent("rocket_command.script_not_found", scriptName))
                    return true
                }

                sender.sendMessage(LocaleManager.getMessageAsComponent("rocket_command.script_disabled", scriptName))
            }
            else -> {
                sender.sendMessage(LocaleManager.getMessageAsComponent("rocket_command.usage"))
            }
        }
        return true
    }
}
