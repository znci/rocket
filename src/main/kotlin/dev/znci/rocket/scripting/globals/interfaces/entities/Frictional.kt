package dev.znci.rocket.scripting.globals.interfaces.entities

import org.bukkit.entity.LivingEntity

@Suppress("unused")
interface Frictional<T> where T : LivingEntity, T: io.papermc.paper.entity.Frictional {
    val entity: T

//    @TwineNativeProperty
//    val frictionState: TriState
//        get() {
//            return TriState(entity.frictionState)
//        }
}