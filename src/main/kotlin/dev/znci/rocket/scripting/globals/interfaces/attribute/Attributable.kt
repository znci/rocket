package dev.znci.rocket.scripting.globals.interfaces.attribute

import org.bukkit.attribute.Attributable
import org.bukkit.entity.LivingEntity

@Suppress("unused")
interface Attributable<T> where T : LivingEntity, T: Attributable {
    val entity: T

    // TODO: Implement this later
//    @TwineNativeFunction
//    fun getAttribute()
}