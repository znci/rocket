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
import dev.znci.rocket.scripting.api.RocketError
import dev.znci.rocket.scripting.api.RocketNative
import dev.znci.rocket.scripting.api.RocketTable
import dev.znci.rocket.scripting.api.annotations.RocketNativeFunction
import dev.znci.rocket.scripting.api.annotations.RocketNativeProperty
import dev.znci.rocket.util.MessageFormatter
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import java.time.Duration

@Suppress("unused")
class LuaPlayers : RocketNative("players") {
    @RocketNativeFunction("get")
    fun getPlayerByName(playerName: String): LuaPlayer {
        val player = Bukkit.getPlayer(playerName) ?: throw RocketError("Player not found")

        return LuaPlayer(player)
    }

    @RocketNativeFunction("getByUUID")
    fun getPlayerByUUID(playerUUID: String): LuaPlayer {
        val player = Bukkit.getPlayer(playerUUID) ?: throw RocketError("Player not found")

        return LuaPlayer(player)
    }

    @RocketNativeFunction("getAll")
    fun getAllPlayers(): List<LuaPlayer> {
        return Bukkit.getOnlinePlayers().map { LuaPlayer(it) }
    }
}

data class TitleTimeTable(
    val fadeIn: Long,
    val stay: Long,
    val fadeOut: Long
) : RocketTable("")

@Suppress("unused")
class LuaPlayer(
    val player: Player
) : RocketNative("") {
    @RocketNativeFunction
    fun send(message: Any): Boolean {
        val messageComponent = MessageFormatter.formatMessage(message.toString())
        player.sendMessage(messageComponent)
        return true
    }

    @RocketNativeFunction
    fun sendActionbar(message: String): Boolean {
        val messageComponent = MessageFormatter.formatMessage(message)
        player.sendActionBar(messageComponent)
        return true
    }

    @RocketNativeFunction
    fun sendTitle(message: String, timeTable: TitleTimeTable): Boolean {
        val messageComponent = MessageFormatter.formatMessage(message)

        val times = Title.Times.times(
            Duration.ofMillis(timeTable.fadeIn * 50),
            Duration.ofMillis(timeTable.stay * 50),
            Duration.ofMillis(timeTable.fadeOut * 50)
        )
        player.sendTitlePart(TitlePart.TITLE, messageComponent)
        player.sendTitlePart(TitlePart.TIMES, times)
        return true
    }

    @RocketNativeFunction
    fun sendSubtitle(message: String, timeTable: TitleTimeTable): Boolean {
        val messageComponent = MessageFormatter.formatMessage(message)
        val times = Title.Times.times(
            Duration.ofMillis(timeTable.fadeIn * 50),
            Duration.ofMillis(timeTable.stay * 50),
            Duration.ofMillis(timeTable.fadeOut * 50)
        )
        player.sendTitlePart(TitlePart.SUBTITLE, messageComponent)
        player.sendTitlePart(TitlePart.TIMES, times)
        return true
    }

    @RocketNativeFunction
    fun setPlayerTime(value: Long, relative: Boolean): Boolean {
        player.setPlayerTime(value, relative)
        return true
    }

    @RocketNativeFunction
    fun addPermission(value: String): Boolean {
        player.addAttachment(
            Bukkit.getPluginManager().getPlugin("Rocket")!!
        ).setPermission(value, true)
        return true
    }

    @RocketNativeFunction
    fun op(): Boolean {
        player.isOp = true
        return true
    }

    @RocketNativeFunction
    fun deop(): Boolean {
        player.isOp = false
        return true
    }

    @RocketNativeFunction
    fun teleport(location: LuaLocation): Boolean {
        player.teleport(location.toBukkitLocation())
        return true
    }

    @RocketNativeFunction
    fun hasPermission(value: String): Boolean {
        println(PermissionsManager.hasPermission(player, value))
        return PermissionsManager.hasPermission(player, value)
    }

    @RocketNativeFunction
    fun isInGroup(value: String): Boolean {
        return PermissionsManager.isPlayerInGroup(player, value)
    }

    @RocketNativeFunction
    fun setGamemode(value: String): Boolean {
        if (false) { // TODO: enum
            throw RocketError("Invalid gamemode")
        }

        player.gameMode = GameMode.valueOf(value)
        return true
    }

    @RocketNativeProperty
    val name: String
        get() {
            return player.name
        }


    @RocketNativeProperty
    val uuid: String
        get() {
            return player.uniqueId.toString()
        }

    @RocketNativeProperty
    val world: String
        get() {
            return player.world.name
        }

    @RocketNativeProperty
    val ip: String?
        get() {
            return player.address?.hostString
        }

    @RocketNativeProperty
    val isFlying: Boolean
        get() {
            return player.isFlying
        }

    @RocketNativeProperty
    val isSneaking: Boolean
        get() {
            return player.isSneaking
        }

    @RocketNativeProperty
    val isSprinting: Boolean
        get() {
            return player.isSprinting
        }

    @RocketNativeProperty
    val isBlocking: Boolean
        get() {
            return player.isBlocking
        }

    @RocketNativeProperty
    val isSleeping: Boolean
        get() {
            return player.isSleeping
        }

    private val block = player.getTargetBlockExact(100)

    @RocketNativeProperty
    var targetBlockType: String = ""

    @RocketNativeProperty
    var targetBlockLocation: Location = Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0)

    @RocketNativeProperty
    var targetBlockLightLevel: Double = 0.0

    @RocketNativeProperty
    var targetBlockTemperature: Double = 0.0

    @RocketNativeProperty
    var targetBlockHumidity: Double = 0.0

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