package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.Rocket
import dev.znci.rocket.scripting.annotations.Global
import dev.znci.rocket.scripting.api.RocketError
import dev.znci.rocket.scripting.classes.worlds.LuaChunk
import dev.znci.rocket.scripting.util.getWorldByNameOrUUID
import dev.znci.twine.TwineNative
import dev.znci.twine.TwineTable
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import dev.znci.twine.annotations.TwineOverload
import net.kyori.adventure.util.TriState
import org.bukkit.TreeType
import org.bukkit.World
import org.bukkit.WorldCreator

data class WorldOptions(
    val seed: Long?,
    val biomeProvider: String?,
    val generateStructures: Boolean?,
    val generator: String?,
    val generatorSettings: String?,
    val hardcore: Boolean?,
    val keepSpawnLoaded: Boolean?,
    val environment: String? // TODO: Change to enum
) : TwineTable("") {
    fun apply(worldCreator: WorldCreator) {
        seed?.let { worldCreator.seed(it) }
        biomeProvider?.let { worldCreator.biomeProvider(it) }
        generateStructures?.let { worldCreator.generateStructures(it) }
        generator?.let { worldCreator.generator(it) }
        generatorSettings?.let { worldCreator.generatorSettings(it) }
        hardcore?.let { worldCreator.hardcore(it) }
        keepSpawnLoaded?.let { worldCreator.keepSpawnLoaded(TriState.byBoolean(it)) }
        environment?.let { worldCreator.environment(World.Environment.valueOf(it)) }
    }
}

@Global
@Suppress("unused")
class LuaWorlds : TwineNative("worlds") {
    @TwineNativeFunction("get")
    fun getWorld(nameOrUUID: String): LuaWorld? {
        return LuaWorld(getWorldByNameOrUUID(nameOrUUID))
    }

    @TwineNativeFunction
    fun create(name: String, options: WorldOptions): LuaWorld {
        val worldCreator = WorldCreator(name)
        options.apply(worldCreator)

        val world = worldCreator.createWorld()
        if (world == null) {
            throw RocketError("Failed to create world $world")
        }

        return LuaWorld(world)
    }

    @TwineNativeFunction
    fun copy(oldWorld: String, newWorld: String, options: WorldOptions): LuaWorld {
        val worldCreator = WorldCreator(newWorld)
        worldCreator.copy(getWorldByNameOrUUID(oldWorld))
        options.apply(worldCreator)

        val world = worldCreator.createWorld()
        if (world == null) {
            throw RocketError("Failed to create world $world")
        }

        return LuaWorld(world)
    }
}

@Suppress("unused")
class LuaWorld(val world: World) : TwineNative("") {
    @TwineNativeProperty
    val canGenerateStructures
        get() = world.canGenerateStructures()

    @TwineNativeProperty
    val allowAnimals
        get() = world.allowAnimals

    @TwineNativeProperty
    val allowMonsters
        get() = world.allowMonsters

    @TwineNativeProperty
    val clearWeatherDuration
        get() = world.clearWeatherDuration

    @TwineNativeProperty
    val coordinateScale = world.coordinateScale

    @TwineNativeProperty
    val difficulty
        get() = world.difficulty.toString() // TODO: Change to enum

    @TwineNativeProperty
    val chunkCount
        get() = world.chunkCount

    @TwineNativeProperty
    val pdc
        get() = world.persistentDataContainer

    @TwineNativeFunction
    fun keepChunkLoaded(x: Int, z: Int, keepLoaded: Boolean) {
        if (keepLoaded) {
            world.addPluginChunkTicket(x, z, Rocket.instance)
        } else {
            world.removePluginChunkTicket(x, z, Rocket.instance)
        }
    }

    @TwineNativeFunction
    @TwineOverload
    fun createExplosion(location: LuaLocation, power: Float) = world.createExplosion(location.toBukkitLocation(), power)

    @TwineNativeFunction
    @TwineOverload
    fun createExplosion(location: LuaLocation, power: Float, setFire: Boolean) = world.createExplosion(location.toBukkitLocation(), power, setFire)

    @TwineNativeFunction
    @TwineOverload
    fun createExplosion(location: LuaLocation, power: Float, setFire: Boolean, breakBlocks: Boolean) = world.createExplosion(location.toBukkitLocation(), power, setFire, breakBlocks)

    @TwineNativeFunction
    fun findLightningRod(location: LuaLocation): LuaLocation? = world.findLightningRod(location.toBukkitLocation())?.let { LuaLocation.fromBukkit(it) }

    @TwineNativeFunction
    fun findLightningTarget(location: LuaLocation): LuaLocation? = world.findLightningTarget(location.toBukkitLocation())?.let { LuaLocation.fromBukkit(it) }

    @TwineNativeFunction
    @TwineOverload
    fun getChunk(x: Int, z: Int): LuaChunk = LuaChunk(world.getChunkAt(x, z))

    @TwineNativeFunction
    @TwineOverload
    fun getChunk(x: Int, z: Int, generate: Boolean) = LuaChunk(world.getChunkAt(x, z, generate))

    @TwineNativeFunction
    fun generateTree(location: LuaLocation, treeType: String) {
        world.generateTree(location.toBukkitLocation(), TreeType.valueOf(treeType)) // TODO: Change to enum
    }

    @TwineNativeFunction
    fun getBiome(x: Int, y: Int, z: Int): String = world.getBiome(x, y, z).toString()
}

