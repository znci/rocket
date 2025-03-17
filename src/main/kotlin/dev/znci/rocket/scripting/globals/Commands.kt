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
package dev.znci.rocket.scripting.globals

import dev.znci.rocket.i18n.LocaleManager
import dev.znci.rocket.scripting.PermissionsManager
import dev.znci.rocket.scripting.PlayerManager
import dev.znci.rocket.scripting.ScriptManager
import dev.znci.rocket.scripting.util.defineProperty
import dev.znci.rocket.util.MessageFormatter
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

class LuaCommands : LuaTable() {
    init {
        set("register", object : TwoArgFunction() {
            override fun call(eventName: LuaValue, callback: LuaValue): LuaValue {
                val commandStr = eventName.tojstring()
                val luaCallback = callback.checkfunction()

                val returnedTable = luaCallback.call()

                val customCommand = object : BukkitCommand(commandStr) {
                    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
                        val table = PlayerManager.getPlayerTable(sender as Player)
                        val luaArgs = convertArgsToLua(args)

                        val referenceFunction = returnedTable.get("reference").checkfunction()
                        val reference = referenceFunction.call()
                        val permission = reference.get("permission").tojstring()
                        val usage = reference.get("usage").tojstring()
                        val permissionMessage = reference.get("permissionMessage").tojstring()
                        val argCount = reference.get("argCount").toint()

                        // If the player does not have permission, show the configured no permission message
                        // or the default no permission message
                        if (permission != "" && PermissionsManager.hasPermission(sender, permission).not()) {
                            var noPermissionMessage = LocaleManager.getMessageAsComponent("no_permission")
                            if (permissionMessage.isNotEmpty()) {
                                noPermissionMessage = MessageFormatter.formatMessage(permissionMessage)
                            }
                            sender.sendMessage(noPermissionMessage)
                            return true
                        }

                        // If no arguments are provided, show the usage
                        if (args.isEmpty() && argCount > 0) {
                            var usageMessage = LocaleManager.getMessageAsComponent("invalid_action")
                            if (usage.isNotEmpty()) {
                                usageMessage = MessageFormatter.formatMessage(usage)
                            }
                            sender.sendMessage(usageMessage)
                            return true
                        }

                        returnedTable.get("executor_function").checkfunction().call(table, luaArgs)
                        return true
                    }
                }

                registerCommand(customCommand)

                return LuaValue.NIL
            }
        })

        set("unregister", object : TwoArgFunction() {
            override fun call(eventName: LuaValue, callback: LuaValue): LuaValue {
                val commandStr = eventName.tojstring()

                unregisterCommand(commandStr)

                ScriptManager.enabledCommands.remove(commandStr)

                return LuaValue.NIL
            }
        })

        set("new", object : ZeroArgFunction() {
            override fun call(): LuaValue {
                val table = LuaTable()
                val commandReference = dev.znci.rocket.scripting.classes.Command(
                    name = "",
                    description = "",
                    usage = "",
                    permission = "",
                    permissionMessage = "",
                    aliases = listOf(""),
                    argCount = 0
                ) { _, _, _, _ -> true }

                table.set("aliases", object : OneArgFunction() {
                    override fun call(arg: LuaValue?): LuaValue {
                        if (arg != null) {
                            val aliases = arg.checktable()
                            for (i in 1..aliases.length()) {
                                commandReference.aliases = commandReference.aliases.plus(aliases.get(i).tojstring())
                            }
                        }

                        return table
                    }
                })

                table.set("executor", object : OneArgFunction() {
                    override fun call(arg: LuaValue?): LuaValue {
                        if (arg != null) {
                            val luaCallback = arg.checkfunction()

                            commandReference.executor = CommandExecutor { sender, _, _, args ->
                                val luaArgs = convertArgsToLua(args)
                                val senderTable = PlayerManager.getPlayerTable(sender as Player)

                                luaCallback.call(senderTable, luaArgs)
                                return@CommandExecutor true
                            }

                            table.set("executor_function", luaCallback)
                        }

                        return table
                    }
                })

                table.set("description", object : OneArgFunction() {
                    override fun call(arg: LuaValue?): LuaValue {
                        if (arg != null) {
                            val description = arg.tojstring()
                            commandReference.description = description
                        }

                        return table
                    }
                })

                table.set("usage", object : OneArgFunction() {
                    override fun call(arg: LuaValue?): LuaValue {
                        if (arg != null) {
                            val usage = arg.tojstring()

                            commandReference.usage = usage
                        }

                        return table
                    }
                })

                table.set("permission", object : OneArgFunction() {
                    override fun call(arg: LuaValue?): LuaValue {
                        if (arg != null) {
                            val permission = arg.tojstring()
                            commandReference.permission = permission
                        }

                        return table
                    }
                })

                table.set("permissionMessage", object : OneArgFunction() {
                    override fun call(arg: LuaValue?): LuaValue {
                        if (arg != null) {
                            val permissionMessage = arg.tojstring()
                            commandReference.permissionMessage = permissionMessage
                        }

                        return table
                    }
                })

                table.set("argCount", object : OneArgFunction() {
                    override fun call(arg: LuaValue?): LuaValue {
                        if (arg != null) {
                            val argCount = arg.toint()
                            commandReference.argCount = argCount
                        }

                        return table
                    }
                })

                table.set("reference", object : ZeroArgFunction() {
                    override fun call(): LuaValue {
                        val commandTable = LuaTable()
                        val luaAliases = LuaTable()
                        val aliases = commandReference.aliases

                        for (i in 1..aliases.size) {
                            luaAliases.set(i, LuaValue.valueOf(aliases[i - 1]))
                        }

                        defineProperty(commandTable, "aliases", { luaAliases })
                        defineProperty(commandTable, "description", { LuaValue.valueOf(commandReference.description) })
                        defineProperty(commandTable, "usage", { LuaValue.valueOf(commandReference.usage) })
                        defineProperty(commandTable, "permission", { LuaValue.valueOf(commandReference.permission) })
                        defineProperty(commandTable, "permissionMessage", { LuaValue.valueOf(commandReference.permissionMessage) })
                        defineProperty(commandTable, "argCount", { LuaValue.valueOf(commandReference.argCount) })

                        return commandTable
                    }
                })

                return table
            }
        })
    }

    private fun registerCommand(command: Command) {
        val commandMap = Bukkit.getServer().javaClass.getDeclaredField("commandMap").apply {
            isAccessible = true
        }.get(Bukkit.getServer()) as org.bukkit.command.CommandMap

        commandMap.register(command.name, command)
    }

    private fun unregisterCommand(commandName: String) {
        val commandMap = Bukkit.getServer().javaClass.getDeclaredField("commandMap").apply {
            isAccessible = true
        }.get(Bukkit.getServer()) as org.bukkit.command.CommandMap

        val existingCommand = commandMap.getCommand(commandName)
        if (existingCommand != null) {
            commandMap.knownCommands.remove(existingCommand.name)
            existingCommand.unregister(commandMap)
        }
    }

    private fun convertArgsToLua(args: Array<out String>): LuaTable {
        val luaArgs = LuaTable()
        for ((index, arg) in args.withIndex()) {
            luaArgs.set(index + 1, LuaValue.valueOf(arg))
        }
        return luaArgs
    }
}