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

import dev.znci.rocket.scripting.PlayerManager
import org.bukkit.Bukkit
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction

class LuaPlayers : LuaTable() {
    init {
        set("get", object : OneArgFunction() {
            override fun call(playerName: LuaValue): LuaValue {
                val player = Bukkit.getPlayer(playerName.tojstring()) ?: return LuaValue.NIL

                return PlayerManager.getPlayerTable(player)
            }
        })
    }
}