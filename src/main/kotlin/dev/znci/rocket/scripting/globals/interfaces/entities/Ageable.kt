package dev.znci.rocket.scripting.globals.interfaces.entities

import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import org.bukkit.entity.LivingEntity

@Suppress("unused")
interface Ageable<T>: Creature<T> where T : LivingEntity, T: org.bukkit.entity.Ageable {
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