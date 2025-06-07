package dev.znci.rocket.scripting.globals.interfaces.entities

import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import org.bukkit.entity.Damageable

@Suppress("unused")
interface Damageable<T> where T: Damageable {
    val entity: T

    @TwineNativeFunction
    fun damage(health: Double) {
        entity.damage(health)
    }

    @TwineNativeProperty
    val health: Double
        get() = entity.health

    @TwineNativeFunction
    fun setHealth(health: Double) {
        entity.health = health
    }

    @TwineNativeFunction
    fun heal(health: Double) {
        entity.heal(health)
    }

    @TwineNativeProperty
    val absorptionAmount: Double
        get() = entity.absorptionAmount

    @TwineNativeFunction
    fun setAbsorptionAmount(amount: Double) {
        entity.absorptionAmount = amount
    }
}