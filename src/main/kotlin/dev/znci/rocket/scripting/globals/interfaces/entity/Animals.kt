package dev.znci.rocket.scripting.globals.interfaces.entity

import dev.znci.rocket.scripting.classes.BaseLuaEntity
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import java.util.UUID

abstract class BaseAnimalEntity<T : org.bukkit.entity.Animals>(entity: T) :
    BaseLuaEntity<T>(entity),
    Animals<T>

@Suppress("unused")
interface Animals<T> : Breedable<T> where T: org.bukkit.entity.Animals {
    @TwineNativeFunction
    fun setBreedCause(entityUUID: String) {
        entity.breedCause = UUID.fromString(entityUUID)
    }

    @TwineNativeProperty
    val loveMode: Boolean
        get() = entity.isLoveMode

    @TwineNativeProperty
    val loveModeTicks: Int
        get() = entity.loveModeTicks

    @TwineNativeFunction
    fun setLoveModeTicks(ticks: Int) {
        entity.loveModeTicks = ticks
    }

    // TODO: Add isBreedItem once ItemStacks are implemented
}