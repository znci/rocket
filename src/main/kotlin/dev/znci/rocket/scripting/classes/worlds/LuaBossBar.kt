package dev.znci.rocket.scripting.classes.worlds

import dev.znci.rocket.scripting.globals.tables.LuaPlayer
import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar

@Suppress("unused")
class LuaBossBar(val bossBar: BossBar) : TwineNative("") {
    @TwineNativeProperty
    var color // TODO: Change to enum
        get() = bossBar.color.toString()
        set(value) { bossBar.color = BarColor.valueOf(value) }

    @TwineNativeProperty
    val players
        get() = bossBar.players.map { LuaPlayer(it) }

    @TwineNativeProperty
    var progress
        get() = bossBar.progress
        set(value) { bossBar.progress = value }

    @TwineNativeProperty
    var style // TODO: Change to enum
        get() = bossBar.style.toString()
        set(value) { bossBar.style = BarStyle.valueOf(value) }

    @TwineNativeProperty
    var title
        get() = bossBar.title
        set(value) { bossBar.setTitle(value) } // No idea why this has to use the set method, but IntelliJ complains about it otherwise

    @TwineNativeProperty
    var visible
        get() = bossBar.isVisible
        set(value) { bossBar.isVisible = value }

    @TwineNativeFunction
    fun addFlag(flag: String) { // TODO: Change to enum
        bossBar.addFlag(BarFlag.valueOf(flag))
    }

    @TwineNativeFunction
    fun addPlayer(player: LuaPlayer) {
        bossBar.addPlayer(player.player)
    }

    @TwineNativeFunction
    fun hasFlag(flag: String): Boolean { // TODO: Change to enum
        return bossBar.hasFlag(BarFlag.valueOf(flag))
    }

    @TwineNativeFunction
    fun removeAllPlayers() {
        bossBar.removeAll()
    }

    @TwineNativeFunction
    fun removeFlag(flag: String) { // TODO: Change to enum
        bossBar.removeFlag(BarFlag.valueOf(flag))
    }

    @TwineNativeFunction
    fun removePlayer(player: LuaPlayer) {
        bossBar.removePlayer(player.player)
    }
}