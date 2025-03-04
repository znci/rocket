package dev.znci.rocket.scripting.functions

import dev.znci.rocket.scripting.PlayerManager
import dev.znci.rocket.scripting.ScriptManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import org.luaj.vm2.LuaFunction
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
                        val table = PlayerManager.getPlayerOverallTable(sender as Player)
                        val luaArgs = convertArgsToLua(args)

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
                    "",
                    "",
                    "",
                    "",
                    listOf(""),
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
                                val senderTable = PlayerManager.getPlayerOverallTable(sender as Player)

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