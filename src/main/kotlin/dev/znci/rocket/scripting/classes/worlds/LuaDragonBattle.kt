package dev.znci.rocket.scripting.classes.worlds

import dev.znci.rocket.scripting.globals.tables.LuaLocation
import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import dev.znci.twine.annotations.TwineOverload
import io.papermc.paper.math.Position
import org.bukkit.boss.DragonBattle

@Suppress("unused")
class LuaDragonBattle(val battle: DragonBattle) : TwineNative("") {
    @TwineNativeProperty
    val bossBar
        get() = LuaBossBar(battle.bossBar)

    @TwineNativeProperty
    val endPortalLocation
        get() = battle.endPortalLocation?.let { LuaLocation.fromBukkit(it) }

    @TwineNativeProperty
    val gatewayCount
        get() = battle.gatewayCount

    @TwineNativeProperty
    var respawnPhase
        get() = battle.respawnPhase.toString()
        set(value) { battle.respawnPhase = DragonBattle.RespawnPhase.valueOf(value) }

    @TwineNativeProperty
    var previouslyBeenKilled
        get() = battle.hasBeenPreviouslyKilled()
        set(value) { battle.setPreviouslyKilled(value) }

    @TwineNativeFunction
    fun generateEndPortal(withPortalBlocks: Boolean) {
        battle.generateEndPortal(withPortalBlocks)
    }

    @TwineNativeFunction
    fun initiateRespawn() {
        battle.initiateRespawn()
    }

    @TwineNativeFunction
    fun resetCrystals() {
        battle.resetCrystals()
    }

    @TwineNativeFunction
    @TwineOverload
    fun spawnGateway() {
        battle.spawnNewGateway()
    }

    @TwineNativeFunction
    @TwineOverload
    fun spawnGateway(location: LuaLocation) {
        val position = Position.block(location.toBukkitLocation())
        battle.spawnNewGateway(position)
    }

    // TODO: Implement getEnderDragon, getHealingCrystals, getRespawnCrystals
}