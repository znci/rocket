package dev.znci.rocket.scripting.classes.entities.interfaces

import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeProperty
import org.bukkit.entity.Entity

class LuaEntity(val entity: Entity) : TwineNative("") {
    @TwineNativeProperty
    val nbtString
        get() = entity.asString
}