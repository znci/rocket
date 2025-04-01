package dev.znci.rocket.scripting.globals.enums

import dev.znci.rocket.scripting.api.RocketEnum
import org.bukkit.GameMode

class GamemodeEnum : RocketEnum("Gamemode") {
    init {
        this.set(
            mapOf(
                "SURVIVAL" to GameMode.SURVIVAL,
                "CREATIVE" to GameMode.CREATIVE,
                "ADVENTURE" to GameMode.ADVENTURE,
                "SPECTATOR" to GameMode.SPECTATOR
            )
        )
    }
}

fun GamemodeEnum.toBukkitGamemode(): GameMode {
    return this.getValue(this.valueName) as GameMode
}