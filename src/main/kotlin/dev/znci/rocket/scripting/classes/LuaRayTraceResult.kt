package dev.znci.rocket.scripting.classes

import dev.znci.rocket.scripting.classes.entities.interfaces.LuaEntity
import dev.znci.twine.TwineNative
import org.bukkit.util.RayTraceResult

class LuaRayTraceResult(val rayTraceResult: RayTraceResult) : TwineNative("") {
    val hitEntity
        get() = rayTraceResult.hitEntity?.let { LuaEntity(it) }

    val hitPosition
        get() = LuaVector(rayTraceResult.hitPosition)
}
