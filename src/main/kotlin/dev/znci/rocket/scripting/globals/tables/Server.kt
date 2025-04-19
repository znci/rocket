package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.Rocket
import dev.znci.rocket.util.MessageFormatter
import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty


@Suppress("unused")
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
        set(value) { server.setWhitelistEnforced(value) }

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
        get() = server.permissionMessage()

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
    fun allowPausing(value: Boolean): Boolean {
        server.allowPausing(Rocket.instance, value)
        return true
    }

    @TwineNativeFunction
    fun broadcast(message: String): Boolean {
        server.broadcast(MessageFormatter.formatMessage(message))
        return true
    }

    @TwineNativeFunction
    fun reload(): Boolean {
        server.reload()
        return true
    }

    @TwineNativeFunction
    fun reloadMinecraftData(): Boolean {
        server.reloadData()
        return true
    }

    @TwineNativeFunction
    fun reloadWhitelist(): Boolean {
        server.reloadWhitelist()
        return true
    }

    @TwineNativeFunction
    fun reloadPermissions(): Boolean {
        server.reloadPermissions()
        return true
    }

    @TwineNativeFunction
    fun reloadCommandAliases(): Boolean {
        server.reloadCommandAliases()
        return true
    }

    @TwineNativeFunction
    fun shutdown(): Boolean {
        server.shutdown()
        return true
    }
}