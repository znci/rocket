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

import dev.znci.rocket.scripting.PermissionsManager
import dev.znci.rocket.scripting.api.RocketLuaValue
import dev.znci.rocket.scripting.api.RocketNative
import dev.znci.rocket.scripting.api.RocketTable
import dev.znci.rocket.scripting.api.annotations.RocketNativeFunction
import dev.znci.rocket.scripting.api.annotations.RocketNativeProperty
import dev.znci.rocket.util.MessageFormatter
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.time.Duration

class LuaPlayers : RocketNative("players") {
    @RocketNativeFunction("get")
    fun getPlayerByName(playerName: String): LuaPlayer? {
        val player = Bukkit.getPlayer(playerName) ?: return null

        return LuaPlayer(player)
    }
}

data class TitleTimeTable(
    val fadeIn: Long,
    val stay: Long,
    val fadeOut: Long
) : RocketTable("")

class LuaPlayer(
    val player: Player
) : RocketNative("") {
    @RocketNativeFunction
    fun send(message: String): RocketLuaValue {
        val messageComponent = MessageFormatter.formatMessage(message)
        player.sendMessage(messageComponent)
        return TRUE
    }

    @RocketNativeFunction
    fun sendActionBar(message: String): RocketLuaValue {
        val messageComponent = MessageFormatter.formatMessage(message)
        player.sendActionBar(messageComponent)
        return TRUE
    }

    @RocketNativeFunction
    fun sendTitle(message: String, timeTable: TitleTimeTable): RocketLuaValue {
        val messageComponent = MessageFormatter.formatMessage(message)

        val times = Title.Times.times(
            Duration.ofMillis(timeTable.get("fadeIn").tolong() * 50),
            Duration.ofMillis(timeTable.get("stay").tolong() * 50),
            Duration.ofMillis(timeTable.get("fadeOut").tolong() * 50)
        )
        player.sendTitlePart(TitlePart.TITLE, messageComponent)
        player.sendTitlePart(TitlePart.TIMES, times)
        return TRUE
    }

    @RocketNativeFunction
    fun sendSubtitle(message: String, timeTable: TitleTimeTable): RocketLuaValue {
        val messageComponent = MessageFormatter.formatMessage(message)
        val times = Title.Times.times(
            Duration.ofMillis(timeTable.fadeIn * 50),
            Duration.ofMillis(timeTable.stay * 50),
            Duration.ofMillis(timeTable.fadeOut * 50)
        )
        player.sendTitlePart(TitlePart.SUBTITLE, messageComponent)
        player.sendTitlePart(TitlePart.TIMES, times)
        return TRUE
    }

    @RocketNativeFunction
    fun setPlayerTime(value: Long, relative: Boolean): RocketLuaValue {
        player.setPlayerTime(value, relative)
        return TRUE
    }

    @RocketNativeFunction
    fun addPermission(value: String): RocketLuaValue {
        player.addAttachment(
            Bukkit.getPluginManager().getPlugin("Rocket")!!
        ).setPermission(value, true)
        return TRUE
    }

    @RocketNativeFunction
    fun op(): RocketLuaValue {
        player.isOp = true
        return TRUE
    }

    @RocketNativeFunction
    fun deop(): RocketLuaValue {
        player.isOp = false
        return TRUE
    }

    @RocketNativeFunction
    fun teleport(location: LuaLocation): RocketLuaValue {
        player.teleport(location.toBukkitLocation())
        return TRUE
    }

    @RocketNativeFunction
    fun hasPermission(value: String): RocketLuaValue {
        return valueOf(
            PermissionsManager.hasPermission(player, value)
        )
    }

    @RocketNativeFunction
    fun isInGroup(value: String): RocketLuaValue {
        return valueOf(
            PermissionsManager.isPlayerInGroup(player, value)
        )
    }

    @RocketNativeProperty
    val name = player.name

    @RocketNativeProperty
    val uuid = player.uniqueId.toString()

    @RocketNativeProperty
    val world = player.world.name

    @RocketNativeProperty
    val ip = player.address?.hostString

    @RocketNativeProperty
    val isFlying = player.isFlying

    @RocketNativeProperty
    val isSneaking = player.isSneaking

    @RocketNativeProperty
    val isSprinting = player.isSprinting

    @RocketNativeProperty
    val isBlocking = player.isBlocking

    @RocketNativeProperty
    val isSleeping = player.isSleeping

    val block = player.getTargetBlockExact(100)
    var targetBlockType = ""
    var targetBlockLocation = Location(Bukkit.getWorld(""), 0.0, 0.0, 0.0)
    var targetBlockLightLevel = 0.0
    var targetBlockTemperature = 0.0
    var targetBlockHumidity = 0.0

    init {
        block?.let {
            targetBlockType = block.type.toString()
            targetBlockLocation = block.location
            targetBlockLightLevel = block.lightLevel.toDouble()
            targetBlockTemperature = block.temperature
            targetBlockHumidity = block.humidity
        }
    }
}