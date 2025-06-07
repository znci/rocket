package dev.znci.rocket.scripting.globals.interfaces.entities

import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty

@Suppress("unused")
interface Breedable<T> : Ageable<T> where T: org.bukkit.entity.Breedable {
    @TwineNativeFunction
    fun setAgeLock(locked: Boolean) {
        entity.ageLock = locked
    }

    @TwineNativeFunction
    fun setBreed(breed: Boolean) {
        entity.setBreed(breed)
    }

    @TwineNativeProperty
    val ageLock: Boolean
        get() = entity.ageLock

    @TwineNativeProperty
    val canBreed: Boolean
        get() = entity.canBreed()
}