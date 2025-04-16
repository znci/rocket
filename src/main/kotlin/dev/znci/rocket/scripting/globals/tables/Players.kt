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
import dev.znci.rocket.util.MessageFormatter
import dev.znci.twine.TwineError
import dev.znci.twine.TwineNative
import dev.znci.twine.TwineTable
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import java.time.Duration

@Suppress("unused")
class LuaPlayers : TwineNative("players") {
    @TwineNativeFunction("get")
    fun getPlayerByName(playerName: String): LuaPlayer {
        val player = Bukkit.getPlayer(playerName)

        if (player == null) {
            throw TwineError("Player not found")
        }

        return LuaPlayer(player)
    }

    @TwineNativeFunction("getByUUID")
    fun getPlayerByUUID(playerUUID: String): LuaPlayer {
        val player = Bukkit.getPlayer(playerUUID)

        if (player == null) {
            throw TwineError("Player not found")
        }

        return LuaPlayer(player)
    }

    @TwineNativeFunction("getAll")
    fun getAllPlayers(): List<LuaPlayer> {
        return Bukkit.getOnlinePlayers().map { LuaPlayer(it) }
    }
}

data class TitleTimeTable(
    val fadeIn: Long,
    val stay: Long,
    val fadeOut: Long
) : TwineTable("")

@Suppress("unused")
class LuaPlayer(
    val player: Player
) : TwineNative("") {
    @TwineNativeFunction
    fun send(message: Any): Boolean {
        val messageComponent = MessageFormatter.formatMessage(message.toString())
        player.sendMessage(messageComponent)
        return true
    }

    @TwineNativeFunction
    fun sendActionbar(message: String): Boolean {
        val messageComponent = MessageFormatter.formatMessage(message)
        player.sendActionBar(messageComponent)
        return true
    }

    @TwineNativeFunction
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

    @TwineNativeFunction
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

    @TwineNativeFunction
    fun setPlayerTime(value: Long, relative: Boolean): Boolean {
        player.setPlayerTime(value, relative)
        return true
    }

    @TwineNativeFunction
    fun addPermission(value: String): Boolean {
        player.addAttachment(
            Bukkit.getPluginManager().getPlugin("Twine")!!
        ).setPermission(value, true)
        return true
    }

    @TwineNativeFunction
    fun op(): Boolean {
        player.isOp = true
        return true
    }

    @TwineNativeFunction
    fun deop(): Boolean {
        player.isOp = false
        return true
    }

    @TwineNativeFunction
    fun teleport(location: LuaLocation): Boolean {
        player.teleport(location.toBukkitLocation())
        return true
    }

    @TwineNativeFunction
    fun hasPermission(value: String): Boolean {
        return PermissionsManager.hasPermission(player, value)
    }

    @TwineNativeFunction
    fun isInGroup(value: String): Boolean {
        return PermissionsManager.isPlayerInGroup(player, value)
    }

    @TwineNativeFunction
    fun setGamemode(value: String): Boolean {
        if (false) { // TODO: enum
            throw TwineError("Invalid gamemode")
        }

        player.gameMode = GameMode.valueOf(value)
        return true
    }

    @TwineNativeProperty
    var name: String
        get() {
            return player.name
        }
        set(value) { return }

    @TwineNativeProperty
    var uuid: String
        get() {
            return player.uniqueId.toString()
        }
        set(value) { return }

    @TwineNativeProperty
    var world: String
        get() {
            return player.world.name
        }
        set(value) { return }

    @TwineNativeProperty
    var ip: String?
        get() {
            return player.address?.hostString
        }
        set(value) { return }

    @TwineNativeProperty
    var isFlying: Boolean
        get() {
            return player.isFlying
        }
        set(value) { return }

    @TwineNativeProperty
    var isSneaking: Boolean
        get() {
            return player.isSneaking
        }
        set(value) { return }

    @TwineNativeProperty
    var isSprinting: Boolean
        get() {
            return player.isSprinting
        }
        set(value) { return }

    @TwineNativeProperty
    var isBlocking: Boolean
        get() {
            return player.isBlocking
        }
        set(value) { return }

    @TwineNativeProperty
    var isSleeping: Boolean
        get() {
            return player.isSleeping
        }
        set(value) { return }

    private val block = player.getTargetBlockExact(100)

    @TwineNativeProperty
    var targetBlockType: String = ""

    @TwineNativeProperty
    var targetBlockLocation: Location = Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0)

    @TwineNativeProperty
    var targetBlockLightLevel: Double = 0.0

    @TwineNativeProperty
    var targetBlockTemperature: Double = 0.0

    @TwineNativeProperty
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