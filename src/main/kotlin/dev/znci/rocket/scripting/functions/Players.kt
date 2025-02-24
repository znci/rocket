package dev.znci.rocket.scripting.functions

import dev.znci.rocket.scripting.PermissionsManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

class LuaPlayers : LuaTable() {
    init {
        set("get", object : OneArgFunction() {
            override fun call(playerName: LuaValue): LuaValue {
                val player = Bukkit.getPlayer(playerName.tojstring()) ?: return LuaValue.NIL

                val playerTable = LuaTable()

                playerTable.set("send", object : OneArgFunction() {
                    override fun call(message: LuaValue): LuaValue? {
                        val messageComponent = Component.text(message.tojstring())
                        player.sendMessage(messageComponent)

                        return LuaValue.TRUE
                    }
                })

                playerTable.set("setXP", object : OneArgFunction() {
                    override fun call(value: LuaValue): LuaValue? {
                        if (!value.isnumber()) return LuaValue.FALSE
                        player.exp = value.tofloat()

                        return LuaValue.TRUE
                    }
                })

                playerTable.set("getXP", object : ZeroArgFunction() {
                    override fun call(): LuaValue {
                        return LuaValue.valueOf(player.exp.toDouble())
                    }
                })

                playerTable.set("setLevel", object : OneArgFunction() {
                    override fun call(value: LuaValue): LuaValue? {
                        if (!value.isnumber()) return LuaValue.FALSE
                        player.level = value.toint()

                        return LuaValue.TRUE
                    }
                })

                playerTable.set("getLevel", object : ZeroArgFunction() {
                    override fun call(): LuaValue {
                        return LuaValue.valueOf(player.level)
                    }
                })

                playerTable.set("setHealth", object : OneArgFunction() {
                    override fun call(value: LuaValue): LuaValue? {
                        if (!value.isnumber()) return LuaValue.FALSE
                        player.health = value.todouble()

                        return LuaValue.TRUE
                    }
                })

                playerTable.set("getHealth", object : ZeroArgFunction() {
                    override fun call(): LuaValue {
                        return LuaValue.valueOf(player.health)
                    }
                })

                playerTable.set("setFoodLevel", object : OneArgFunction() {
                    override fun call(value: LuaValue): LuaValue? {
                        if (!value.isnumber()) return LuaValue.FALSE
                        player.foodLevel = value.toint()

                        return LuaValue.TRUE
                    }
                })

                playerTable.set("getFoodLevel", object : ZeroArgFunction() {
                    override fun call(): LuaValue {
                        return LuaValue.valueOf(player.foodLevel)
                    }
                })

                playerTable.set("setSaturation", object : OneArgFunction() {
                    override fun call(value: LuaValue): LuaValue? {
                        if (!value.isnumber()) return LuaValue.FALSE
                        player.saturation = value.tofloat()

                        return LuaValue.TRUE
                    }
                })

                playerTable.set("getSaturation", object : ZeroArgFunction() {
                    override fun call(): LuaValue {
                        return LuaValue.valueOf(player.saturation.toDouble())
                    }
                })

                playerTable.set("setExhaustion", object : OneArgFunction() {
                    override fun call(value: LuaValue): LuaValue? {
                        if (!value.isnumber()) return LuaValue.FALSE
                        player.exhaustion = value.tofloat()

                        return LuaValue.TRUE
                    }
                })

                playerTable.set("getExhaustion", object : ZeroArgFunction() {
                    override fun call(): LuaValue {
                        return LuaValue.valueOf(player.exhaustion.toDouble())
                    }
                })

                playerTable.set("setGameMode", object : OneArgFunction() {
                    override fun call(value: LuaValue): LuaValue? {
                        if (!value.isnumber()) return LuaValue.FALSE
                        player.gameMode = GameMode.entries.toTypedArray()[value.toint()]

                        return LuaValue.TRUE
                    }
                })

                playerTable.set("getGameMode", object : ZeroArgFunction() {
                    override fun call(): LuaValue {
                        return LuaValue.valueOf(player.gameMode.ordinal)
                    }
                })

                playerTable.set("setDisplayName", object : OneArgFunction() {
                    override fun call(value: LuaValue): LuaValue? {
                        player.displayName(Component.text(value.tojstring()))

                        return LuaValue.TRUE
                    }
                })

                playerTable.set("getDisplayName", object : ZeroArgFunction() {
                    override fun call(): LuaValue {
                        return LuaValue.valueOf(player.displayName().toString())
                    }
                })

                playerTable.set("setTabListName", object : OneArgFunction() {
                    override fun call(value: LuaValue): LuaValue? {
                        player.playerListName(Component.text(value.tojstring()))

                        return LuaValue.TRUE
                    }
                })

                playerTable.set("getTabListName", object : ZeroArgFunction() {
                    override fun call(): LuaValue {
                        return LuaValue.valueOf(player.playerListName().toString())
                    }
                })

                playerTable.set("hasPermission", object : OneArgFunction() {
                    override fun call(value: LuaValue): LuaValue? {
                        return LuaValue.valueOf(
                            PermissionsManager.hasPermission(player, value.tojstring())
                        )
                    }
                })

                return playerTable
            }
        })
    }
}