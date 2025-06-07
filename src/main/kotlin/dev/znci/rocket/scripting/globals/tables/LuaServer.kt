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

import dev.znci.rocket.Rocket
import dev.znci.rocket.scripting.annotations.Global
import dev.znci.rocket.util.MessageFormatter
import dev.znci.rocket.util.MessageFormatter.toMiniMessage
import dev.znci.twine.nativex.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty


@Suppress("unused")
@Global
class LuaServer : TwineNative("server") {
    private val server = Rocket.instance.server

    @TwineNativeProperty
    val ip
        get() = server.ip

    @TwineNativeProperty
    val port
        get() = server.port

    @TwineNativeProperty
    var maxPlayers
        get() = server.maxPlayers
        set(value) { server.maxPlayers = value}

    @TwineNativeProperty
    val maxWorldSize
        get() = server.maxWorldSize

    @TwineNativeProperty
    val minecraftVersion
        get() = server.version

    @TwineNativeProperty
    val operators
        get() = server.operators.map { LuaOfflinePlayer(it) }

    @TwineNativeProperty
    val resourcePack
        get() = server.resourcePack

    @TwineNativeProperty
    val resourcePackHash
        get() = server.resourcePackHash

    @TwineNativeProperty
    val resourcePackPrompt
        get() = server.resourcePackPrompt

    @TwineNativeProperty
    val resourcePackRequired
        get() = server.isResourcePackRequired

    @TwineNativeProperty
    val spawnProtectionRadius
        get() = server.spawnRadius

    @TwineNativeProperty
    val tps
        get() = ArrayList(server.tps.toList())

    @TwineNativeProperty
    val tickTimes
        get() = ArrayList(server.tickTimes.toList())

    @TwineNativeProperty
    val viewDistance
        get() = server.viewDistance

    @TwineNativeProperty
    var whitelisted
        get() = server.hasWhitelist()
        set(value) { server.setWhitelist(value) }

    @TwineNativeProperty
    var whitelistEnforced
        get() = server.isWhitelistEnforced
        set(value) { server.isWhitelistEnforced = value }

    @TwineNativeProperty
    val acceptingTransfers
        get() = server.isAcceptingTransfers

    @TwineNativeProperty
    val enforcingSecureProfiles
        get() = server.isEnforcingSecureProfiles

    @TwineNativeProperty
    val hardcore
        get() = server.isHardcore

    @TwineNativeProperty
    val loggingIPs
        get() = server.isLoggingIPs

    @TwineNativeProperty
    val stopping
        get() = server.isStopping

    @TwineNativeProperty
    val permissionMessage
        get() = server.permissionMessage().toMiniMessage()

    // TODO: implement set functionality once enums are done
    @TwineNativeProperty
    val defaultGamemode
        get() = server.defaultGameMode.toString()

    @TwineNativeProperty
    val paused
        get() = server.isPaused

    @TwineNativeProperty
    var pauseWhenEmptyTime
        get() = server.pauseWhenEmptyTime
        set(value) { server.pauseWhenEmptyTime = value }

    @TwineNativeProperty
    val allowEnd
        get() = server.allowEnd

    @TwineNativeProperty
    val allowNether
        get() = server.allowNether

    @TwineNativeProperty
    val allowFlight
        get() = server.allowFlight

    @TwineNativeProperty
    val idleTimeout
        get() = server.idleTimeout

    @TwineNativeFunction
    fun allowPausing(value: Boolean) = server.allowPausing(Rocket.instance, value)

    @TwineNativeFunction
    fun broadcast(message: String) = server.broadcast(MessageFormatter.formatMessage(message))

    @TwineNativeFunction
    fun reload() = server.reload()

    @TwineNativeFunction
    fun reloadMinecraftData() = server.reloadData()

    @TwineNativeFunction
    fun reloadWhitelist() = server.reloadWhitelist()

    @TwineNativeFunction
    fun reloadPermissions() = server.reloadPermissions()

    @TwineNativeFunction
    fun reloadCommandAliases() = server.reloadCommandAliases()

    @TwineNativeFunction
    fun shutdown() = server.shutdown()

}