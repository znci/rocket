package dev.znci.rocket.scripting.functions

import dev.znci.rocket.scripting.PermissionsManager
import dev.znci.rocket.scripting.PlayerManager
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

                return PlayerManager.getPlayerOverallTable(player)
            }
        })
    }
}