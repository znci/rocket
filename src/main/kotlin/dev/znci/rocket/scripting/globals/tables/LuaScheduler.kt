package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.Rocket
import dev.znci.rocket.scripting.annotations.Global
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.nativex.TwineNative
import org.bukkit.scheduler.BukkitRunnable

@Suppress("unused")
@Global
class LuaScheduler : TwineNative("scheduler") {
    @TwineNativeFunction
    fun runLater(ticks: Long, callback: () -> Unit) {
        object : BukkitRunnable() {
            override fun run() {
                callback()
            }
        }.runTaskLater(
            Rocket.instance, ticks
        )
    }
}