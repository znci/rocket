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
import dev.znci.rocket.scripting.annotations.Global
import dev.znci.rocket.util.MessageFormatter
import dev.znci.twine.TwineNative
import dev.znci.twine.TwineTable
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.time.Duration
import java.util.*

@Suppress("unused")
@Global
class LuaPlayers : TwineNative("players") {
    @TwineNativeFunction("get")
    fun getPlayerByName(playerName: String): LuaPlayer? {
        return Bukkit.getPlayer(playerName)?.let { LuaPlayer(it) }
    }

    @TwineNativeFunction("getAll")
    fun getAllPlayers(): List<LuaPlayer> {
        return Bukkit.getOnlinePlayers().map { LuaPlayer(it) }
    }

    @TwineNativeFunction("getOfflinePlayer")
    fun getOfflinePlayer(playerName: String): LuaOfflinePlayer {
        return LuaOfflinePlayer(Bukkit.getOfflinePlayer(playerName))
    }

    @TwineNativeFunction("getOfflinePlayerByUUID")
    fun getOfflinePlayerByUUID(playerUUID: String): LuaOfflinePlayer {
        return LuaOfflinePlayer(Bukkit.getOfflinePlayer(UUID.fromString(playerUUID)))
    }

    @TwineNativeFunction("getCachedOfflinePlayer")
    fun getCachedOfflinePlayer(playerName: String): LuaOfflinePlayer? {
        val offlinePlayer = Bukkit.getOfflinePlayerIfCached(playerName)

        return if (offlinePlayer == null) {
            null
        } else {
            LuaOfflinePlayer(offlinePlayer)
        }
    }

    @TwineNativeFunction("getAllOfflinePlayers")
    fun getAllOfflinePlayers(): List<LuaOfflinePlayer> {
        return Bukkit.getOfflinePlayers().map { LuaOfflinePlayer(it) }
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
) : LuaOfflinePlayer(player) {
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
        player.gameMode = GameMode.valueOf(value)
        return true
    }

    @TwineNativeProperty
    override val location: LuaLocation
        get() {
            return LuaLocation.fromBukkit(player.location)
        }


    @TwineNativeProperty
    override val name: String
        get() {
            return player.name
        }

    @TwineNativeProperty
    val world: String
        get() {
            return player.world.name
        }

    @TwineNativeProperty
    val ip: String?
        get() {
            return player.address?.hostString
        }

    @TwineNativeProperty
    val isFlying: Boolean
        get() {
            return player.isFlying
        }

    @TwineNativeProperty
    val isSneaking: Boolean
        get() {
            return player.isSneaking
        }

    @TwineNativeProperty
    val isSprinting: Boolean
        get() {
            return player.isSprinting
        }

    @TwineNativeProperty
    val isBlocking: Boolean
        get() {
            return player.isBlocking
        }

    @TwineNativeProperty
    val isSleeping: Boolean
        get() {
            return player.isSleeping
        }

    private val targetBlock
        get() = player.getTargetBlockExact(100)

    @TwineNativeProperty
    val targetBlockType
        get() = targetBlock?.type.toString()

    @TwineNativeProperty
    val targetBlockLocation
        get() = LuaLocation.fromBukkit(targetBlock?.location!!)

    @TwineNativeProperty
    val targetBlockLightLevel
        get() = targetBlock?.lightLevel

    @TwineNativeProperty
    val targetBlockTemperature
        get() = targetBlock?.temperature

    @TwineNativeProperty
    val targetBlockHumidity
        get() = targetBlock?.humidity
}

@Suppress("unused")
open class LuaOfflinePlayer(
    private val offlinePlayer: OfflinePlayer
) : TwineNative("") {
    @TwineNativeProperty
    val firstPlayed
        get() = offlinePlayer.firstPlayed

    @TwineNativeProperty
    val lastDeathLocation
        get() = offlinePlayer.lastDeathLocation?.let { LuaLocation.fromBukkit(it) }

    @TwineNativeProperty
    val lastLogin
        get() = offlinePlayer.lastLogin

    @TwineNativeProperty
    val lastSeen
        get() = offlinePlayer.lastSeen

    // location, name, and player are open because they are overridden in LuaPlayer with non-nullable values
    // in the case of location and name and the actual player instance in the case of player
    @TwineNativeProperty
    open val location
        get() = offlinePlayer.location?.let { LuaLocation.fromBukkit(it) }

    @TwineNativeProperty
    open val name
        get() = offlinePlayer.name

    @TwineNativeProperty("player")
    open val playerLuaProperty
        get() = offlinePlayer.player?.let { LuaPlayer(it) }

    @TwineNativeProperty
    val respawnLocation
        get() = offlinePlayer.respawnLocation?.let { LuaLocation.fromBukkit(it) }

    @TwineNativeProperty
    val uuid
        get() = offlinePlayer.uniqueId.toString()

    @TwineNativeProperty
    val hasPlayedBefore
        get() = offlinePlayer.hasPlayedBefore()

    @TwineNativeProperty
    val banned
        get() = offlinePlayer.isBanned

    @TwineNativeProperty
    val connected
        get() = offlinePlayer.isConnected

    @TwineNativeProperty
    val online
        get() = offlinePlayer.isOnline

    @TwineNativeProperty
    var whitelisted
        get() = offlinePlayer.isWhitelisted
        set(value) { offlinePlayer.isWhitelisted = value }
}