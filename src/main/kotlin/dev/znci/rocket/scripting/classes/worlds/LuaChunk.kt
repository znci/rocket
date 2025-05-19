package dev.znci.rocket.scripting.classes.worlds

import dev.znci.rocket.scripting.globals.tables.LuaPlayer
import dev.znci.rocket.scripting.globals.tables.LuaWorld
import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import dev.znci.twine.annotations.TwineOverload
import org.bukkit.Chunk

@Suppress("unused")
class LuaChunk(val chunk: Chunk) : TwineNative("") {
    @TwineNativeProperty
    val chunkKey
        get() = chunk.chunkKey

    @TwineNativeProperty
    var inhabitedTime
        get() = chunk.inhabitedTime
        set(value) { chunk.inhabitedTime = value }

    @TwineNativeProperty
    val playersSeeingChunk
        get() = chunk.playersSeeingChunk.toTypedArray().map { LuaPlayer(it) }

    @TwineNativeProperty
    val pluginsLoadingChunk
        get() = chunk.pluginChunkTickets.toTypedArray().map { it.name }

    @TwineNativeProperty
    val world
        get() = LuaWorld(chunk.world)

    @TwineNativeProperty
    val x
        get() = chunk.x

    @TwineNativeProperty
    val z
        get() = chunk.z

    @TwineNativeProperty
    val entitiesLoaded
        get() = chunk.isEntitiesLoaded

    @TwineNativeProperty
    var forceLoaded
        get() = chunk.isForceLoaded
        set(value) { chunk.isForceLoaded = value }

    @TwineNativeProperty
    val generated
        get() = chunk.isGenerated

    @TwineNativeProperty
    val loaded
        get() = chunk.isLoaded

    @TwineNativeProperty
    val slimeChunk
        get() = chunk.isSlimeChunk

    @TwineNativeProperty
    val loadLevel
        get() = chunk.loadLevel.toString()

    @TwineNativeFunction
    @TwineOverload
    fun load() {
        chunk.load()
    }

    @TwineNativeFunction
    @TwineOverload
    fun load(generate: Boolean) {
        chunk.load(generate)
    }

    @TwineNativeFunction
    @TwineOverload
    fun unload() {
        chunk.unload()
    }

    @TwineNativeFunction
    @TwineOverload
    fun unload(save: Boolean) {
        chunk.unload(save)
    }

    @TwineNativeFunction
    @TwineOverload
    fun getStructures() = chunk.structures.toTypedArray().map { it.structure.toString() } // TODO: Change to GeneratedStructure class once BoundingBox is implemented

    private val snapshot
        get() = chunk.getChunkSnapshot(true, true, true)

    @TwineNativeFunction
    fun getBlockEmittedLight(x: Int, y: Int, z: Int) = snapshot.getBlockEmittedLight(x, y, z)

    @TwineNativeFunction
    fun getBlockSkyLight(x: Int, y: Int, z: Int) = snapshot.getBlockSkyLight(x, y, z)

    @TwineNativeFunction
    fun getHighestBlockAt(x: Int, z: Int) = snapshot.getHighestBlockYAt(x, z)

    @TwineNativeFunction
    fun getBiomeTemperature(x: Int, y: Int, z: Int) = snapshot.getRawBiomeTemperature(x, y, z)

    @TwineNativeFunction
    fun isSectionEmpty(sectionY: Int) = snapshot.isSectionEmpty(sectionY)

    @TwineNativeFunction
    fun getBiome(x: Int, y: Int, z: Int) = snapshot.getBiome(x, y, z).toString() // TODO: Change to enum

    // TODO: Implement contains, getBlock, getEntities, getTileEntities
}