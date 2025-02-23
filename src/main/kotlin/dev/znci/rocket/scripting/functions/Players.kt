package dev.znci.rocket.scripting.functions

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction

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

                return playerTable
            }
        })
    }
}