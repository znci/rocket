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

import dev.znci.rocket.scripting.api.RocketLuaValue
import dev.znci.rocket.scripting.api.RocketNative
import dev.znci.rocket.scripting.api.annotations.RocketNativeFunction
import dev.znci.rocket.util.MessageFormatter
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class LuaPlayers : RocketNative("players") {
    @RocketNativeFunction("get")
    fun getPlayerByName(playerName: String): RocketLuaValue {
        val player = Bukkit.getPlayer(playerName) ?: return NIL

        println(player)
        println(LuaPlayer(player).table)

        return LuaPlayer(player)
    }
}

class LuaPlayer(
    val player: Player
) : RocketNative("") {
    fun send(message: String): RocketLuaValue {
        val messageComponent = MessageFormatter.formatMessage(message)
        player.sendMessage(messageComponent)
        return TRUE
    }
}