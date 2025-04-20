package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.scripting.classes.LuaBoundingBox
import dev.znci.rocket.scripting.classes.LuaVector
import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction

class LuaPhysics : TwineNative("physics") {
    @TwineNativeFunction
    fun newBoundingBox(
        minX: Double,
        minY: Double,
        minZ: Double,
        maxX: Double,
        maxY: Double,
        maxZ: Double
    ): LuaBoundingBox {
        return LuaBoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
    }

    @TwineNativeFunction
    fun newVector(
        x: Double,
        y: Double,
        z: Double
    ): LuaVector {
        return LuaVector(x, y, z)
    }
}