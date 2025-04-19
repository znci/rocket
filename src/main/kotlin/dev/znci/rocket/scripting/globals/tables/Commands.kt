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
package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.i18n.LocaleManager
import dev.znci.rocket.scripting.PermissionsManager
import dev.znci.rocket.scripting.classes.CommandReference
import dev.znci.rocket.util.MessageFormatter
import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.luaj.vm2.LuaTable

class LuaCommands : TwineNative("commands") {
    @TwineNativeFunction
    fun new(): LuaCommand {
        return LuaCommand()
    }
}

class LuaCommand() : TwineNative("") {
    val commandReference = CommandReference()

    @TwineNativeFunction
    fun register(): LuaCommand {
        try {
            // TODO: Add alias support
            unregisterCommand(commandReference.name)
            val command = object : Command(commandReference.name) {
                override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
                    val player = sender as Player

                    // Show the no permission message if a permission is provided
                    if (commandReference.permission != "" && PermissionsManager.hasPermission(sender, commandReference.permission)) {
                        var noPermissionMessage = LocaleManager.getMessageAsComponent("no_permission")

                        // Uses the user-specified no permission message if it is provided
                        if (commandReference.permissionMessage.isNotEmpty()) {
                            noPermissionMessage = MessageFormatter.formatMessage(commandReference.permissionMessage)
                        }

                        player.sendMessage(noPermissionMessage)
                        return true
                    }

                    // Show the no args message if none are provided and the argCount is above 0
                    if (args.isEmpty() && commandReference.argCount > 0) {
                        var usageMessage = LocaleManager.getMessageAsComponent("invalid_action")
                        if (commandReference.usage.isNotEmpty()) {
                            usageMessage = MessageFormatter.formatMessage(commandReference.usage)
                        }
                        player.sendMessage(usageMessage)
                        return true
                    }

                    commandReference.executor.onCommand(sender, this, "", args)
                    return true
                }

                override fun tabComplete(
                    sender: CommandSender,
                    alias: String,
                    args: Array<out String>
                ): List<String?> {
                    val returnedCompletions = commandReference.tabCompleter.onTabComplete(sender, this, "", args)
                        ?.filterNotNull()
                        ?: emptyList()

                    return returnedCompletions
                }
            }

            // Use reflection to make the commandMap accessible to forcefully add the command
            val commandMap = Bukkit.getServer().javaClass.getDeclaredField("commandMap").apply {
                isAccessible = true
            }.get(Bukkit.getServer()) as CommandMap

            commandMap.register(commandReference.fallbackPrefix, command)

            updateCommandsForAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    private fun updateCommandsForAll() {
        Bukkit.getServer().onlinePlayers.forEach { player ->
            player.updateCommands()
        }
    }

    private fun unregisterCommand(commandName: String): Boolean {
        try {
            val commandMap = Bukkit.getServer().javaClass.getDeclaredField("commandMap").apply {
                isAccessible = true
            }.get(Bukkit.getServer()) as CommandMap

            val existingCommand = commandMap.getCommand(commandName)
            if (existingCommand != null) {
                commandMap.knownCommands.remove(existingCommand.name)
                existingCommand.unregister(commandMap)
            }

            return true
        } catch (e: Exception) {
            // The command *probably* isn't in existence, so it throws an error
            return false
        }
    }

    @TwineNativeFunction
    fun aliases(aliasList: Array<String>): LuaCommand {
        for (i in 1..aliasList.size) {
            commandReference.aliases = commandReference.aliases.plus(aliasList[i])
        }
        return this
    }

    @TwineNativeFunction
    fun executor(callback: (sender: LuaPlayer, args: Array<String>) -> Unit): LuaCommand {
        commandReference.executor = CommandExecutor { commandSender, _, _, commandArgs ->
            callback(LuaPlayer(commandSender as Player), commandArgs)
            return@CommandExecutor true
        }
        return this
    }

    @TwineNativeFunction
    fun tabCompleter(callback: (sender: LuaPlayer, args: Array<String>) -> Any): LuaCommand {
        commandReference.tabCompleter = TabCompleter { commandSender, _, _, commandArgs ->
            val result = callback(LuaPlayer(commandSender as Player), commandArgs)
            when (result) {
                is LuaTable -> {
                    val list = List(result.rawlen()) { i ->
                        result.get(i + 1).tojstring()
                    }
                    list
                }
                else -> emptyList()
            }
        }
        return this
    }

    @TwineNativeFunction
    fun name(text: String): LuaCommand {
        commandReference.name = text
        return this
    }

    @TwineNativeFunction
    fun description(text: String): LuaCommand {
        commandReference.description = text
        return this
    }

    @TwineNativeFunction
    fun usage(text: String): LuaCommand {
        commandReference.usage = text
        return this
    }

    @TwineNativeFunction
    fun permission(text: String): LuaCommand {
        commandReference.permission = text
        return this
    }

    @TwineNativeFunction
    fun permissionMessage(text: String): LuaCommand {
        commandReference.permissionMessage = text
        return this
    }

    @TwineNativeFunction
    fun argCount(value: Int): LuaCommand {
        commandReference.argCount = value
        return this
    }

    @TwineNativeFunction
    fun fallbackPrefix(value: String): LuaCommand {
        commandReference.fallbackPrefix = value
        return this
    }
}