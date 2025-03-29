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
import org.bukkit.Location
import org.bukkit.entity.Player
import java.time.Duration

class LuaPlayers : RocketNative("players") {
    @RocketNativeFunction("get")
    fun getPlayerByName(playerName: String): LuaPlayer? {
        val player = Bukkit.getPlayer(playerName)

        if (player == null) {
            throw RocketError("Player not found")
        }

        return LuaPlayer(player)
    }

    @RocketNativeFunction("getByUUID")
    fun getPlayerByUUID(playerUUID: String): LuaPlayer? {
        val player = Bukkit.getPlayer(playerUUID)

        if (player == null) {
            throw RocketError("Player not found")
        }

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

class LuaPlayer(
    val player: Player
) : RocketNative("") {
    @RocketNativeFunction
    fun send(message: String): Boolean {
        val messageComponent = MessageFormatter.formatMessage(message)
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
            Duration.ofMillis(timeTable.get("fadeIn").tolong() * 50),
            Duration.ofMillis(timeTable.get("stay").tolong() * 50),
            Duration.ofMillis(timeTable.get("fadeOut").tolong() * 50)
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

    @RocketNativeProperty
    var name: String
        get() {
            return player.name
        }
        set(value) { return }

    @RocketNativeProperty
    var uuid: String
        get() {
            return player.uniqueId.toString()
        }
        set(value) { return }

    @RocketNativeProperty
    var world: String
        get() {
            return player.world.name
        }
        set(value) { return }

    @RocketNativeProperty
    var ip: String?
        get() {
            return player.address?.hostString
        }
        set(value) { return }

    @RocketNativeProperty
    var isFlying: Boolean
        get() {
            return player.isFlying
        }
        set(value) { return }

    @RocketNativeProperty
    var isSneaking: Boolean
        get() {
            return player.isSneaking
        }
        set(value) { return }

    @RocketNativeProperty
    var isSprinting: Boolean
        get() {
            return player.isSprinting
        }
        set(value) { return }

    @RocketNativeProperty
    var isBlocking: Boolean
        get() {
            return player.isBlocking
        }
        set(value) { return }

    @RocketNativeProperty
    var isSleeping: Boolean
        get() {
            return player.isSleeping
        }
        set(value) { return }

    val block = player.getTargetBlockExact(100)

    @RocketNativeProperty
    var targetBlockType: String = ""
        get() {
            return field
        }
        set(value) { return }

    @RocketNativeProperty
    var targetBlockLocation: Location = Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0)
        get() {
            return field
        }
        set(value) { return }

    @RocketNativeProperty
    var targetBlockLightLevel: Double = 0.0
        get() {
            return field
        }
        set(value) { return }

    @RocketNativeProperty
    var targetBlockTemperature: Double = 0.0
        get() {
            return field
        }
        set(value) { return }

    @RocketNativeProperty
    var targetBlockHumidity: Double = 0.0
        get() {
            return field
        }
        set(value) { return }

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