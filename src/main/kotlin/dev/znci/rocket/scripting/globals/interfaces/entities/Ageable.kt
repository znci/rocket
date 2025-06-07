package dev.znci.rocket.scripting.globals.interfaces.entities

import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty

@Suppress("unused")
interface Ageable<T>: Creature<T> where T: org.bukkit.entity.Ageable {
    @TwineNativeProperty
    val age: Int
        get() = entity.age

    @TwineNativeFunction
    fun setAge(age: Int) {
        entity.age = age
    }

    @TwineNativeFunction
    fun setBaby() {
        entity.setBaby()
    }

    @TwineNativeFunction
    fun setAdult() {
        entity.setAdult()
    }

    @TwineNativeProperty
    val adult: Boolean
        get() = entity.isAdult
}