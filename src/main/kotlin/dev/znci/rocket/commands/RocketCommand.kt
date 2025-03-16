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
package dev.znci.rocket.commands

import dev.znci.rocket.i18n.LocaleManager
import dev.znci.rocket.scripting.ScriptManager
import dev.znci.rocket.scripting.ScriptManager.disableFile
import dev.znci.rocket.scripting.ScriptManager.scriptsFolder
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class RocketCommand(private val plugin: JavaPlugin) : TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size != 2) {
            sender.sendMessage(LocaleManager.getMessageAsComponent("rocket_command.usage"))
            return true
        }

        val action = args[0].lowercase()
        val scriptName = if (!args[1].endsWith(".lua")) "${args[1]}.lua" else args[1]

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
                } else if (scriptName.lowercase() == "all") {
                    val results = ScriptManager.loadAll()
                    if (results.isNotEmpty()) {
                        results.forEach { error ->
                            sender.sendMessage(LocaleManager.getMessageAsComponent("generic_error", error ?: "Unknown error"))
                        }
                    }
                }

                val scriptFile = File(scriptsFolder, scriptName)
                if (!scriptFile.exists()) {
                    sender.sendMessage(LocaleManager.getMessageAsComponent("rocket_command.script_not_found", scriptName))
                    return true
                }

                val result = ScriptManager.loadScript(scriptFile)

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

                // TODO: Add disabling of file (with '-')
                //          For another PR though

                disableFile(scriptFile)

                sender.sendMessage(LocaleManager.getMessageAsComponent("rocket_command.script_disabled", scriptName))
            }
            else -> {
                sender.sendMessage(LocaleManager.getMessageAsComponent("rocket_command.usage"))
            }
        }
        return true
    }



    @Suppress("SENSELESS_COMPARISON")
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>? {

        if (!sender.isOp) return null
        if (args.size == 1) {
            return mutableListOf("reload", "disable")
        } else if (args.size == 2) {
            val list: MutableList<String>? =
                if (args[0] == "reload") ScriptManager.getAllScripts().toMutableList().also { it.add("config") }
                else if (args[0] == "disable") ScriptManager.getAllScripts(false).toMutableList().also { it.add("config") }
                else null
            return list
        }
        return null

    }

}
