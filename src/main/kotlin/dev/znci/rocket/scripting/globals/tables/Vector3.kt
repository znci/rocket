package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.scripting.annotations.Global
import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import dev.znci.twine.annotations.TwineOverload
import org.bukkit.util.Vector

@Global
class LuaVectors3 : TwineNative("vector3") {
    @TwineNativeFunction
    @TwineOverload
    fun new(x: Double, y: Double, z: Double): LuaVector3 {
        return LuaVector3(x, y, z)
    }
}

class LuaVector3(
    x: Double,
    y: Double,
    z: Double
) : TwineNative("") {
    private var bukkitVector = Vector(x, y, z)

    @TwineNativeFunction
    fun add(vector3: LuaVector3): LuaVector3 {
        bukkitVector.add(vector3.bukkitVector)
        return this
    }

    @TwineNativeFunction
    fun set(vector3: LuaVector3): LuaVector3 {
        bukkitVector = vector3.bukkitVector
        return this
    }

    @TwineNativeFunction
    fun clone(): LuaVector3 {
        return bukkitVector.toLuaVector3()
    }

    @TwineNativeFunction
    fun crossProduct(comparedVector: LuaVector3): LuaVector3 {
        // TODO: Evaluate whether getCrossProduct or crossProduct is better here
        return bukkitVector.getCrossProduct(
            comparedVector.bukkitVector
        ).toLuaVector3()
    }

    @TwineNativeFunction
    fun cross(comparedVector: LuaVector3): LuaVector3 {
        return crossProduct(comparedVector)
    }

    @TwineNativeFunction
    fun distance(comparedVector: LuaVector3): Double {
        return bukkitVector.distance(
            comparedVector.bukkitVector
        )
    }

    @TwineNativeFunction
    fun divide(comparedVector: LuaVector3): LuaVector3 {
        return bukkitVector.divide(
            comparedVector.bukkitVector
        ).toLuaVector3()
    }

    @TwineNativeFunction
    fun div(comparedVector: LuaVector3): LuaVector3 {
        return divide(comparedVector)
    }

    @TwineNativeFunction
    fun multiply(comparedVector: LuaVector3): LuaVector3 {
        return bukkitVector.multiply(
            comparedVector.bukkitVector
        ).toLuaVector3()
    }

    @TwineNativeFunction
    fun mul(comparedVector: LuaVector3): LuaVector3 {
        return multiply(comparedVector)
    }

    @TwineNativeFunction
    fun subtract(comparedVector: LuaVector3): LuaVector3 {
        return bukkitVector.subtract(
            comparedVector.bukkitVector
        ).toLuaVector3()
    }

    @TwineNativeFunction
    fun sub(comparedVector: LuaVector3): LuaVector3 {
        return subtract(comparedVector)
    }

    @TwineNativeFunction
    fun dotProduct(comparedVector: LuaVector3): Double {
        return bukkitVector.dot(
            comparedVector.bukkitVector
        )
    }

    @TwineNativeFunction
    fun dot(comparedVector: LuaVector3): Double {
        return dotProduct(comparedVector)
    }

    @TwineNativeFunction
    fun midpoint(comparedVector: LuaVector3): LuaVector3 {
        return bukkitVector.getMidpoint(
            comparedVector.bukkitVector
        ).toLuaVector3()
    }

    @TwineNativeFunction
    fun inAABB(min: LuaVector3, max: LuaVector3): Boolean {
        return bukkitVector.isInAABB(min.bukkitVector, max.bukkitVector)
    }

    @TwineNativeFunction
    fun inSphere(center: LuaVector3, radius: Double): Boolean {
        return bukkitVector.isInSphere(center.bukkitVector, radius)
    }

    @TwineNativeFunction
    fun rotateAround(comparedVector: LuaVector3, angle: Double): LuaVector3 {
        return this.bukkitVector.rotateAroundAxis(
            comparedVector.bukkitVector, angle
        ).toLuaVector3()
    }

    @TwineNativeProperty("magnitude")
    val magnitude: Double
        get() = bukkitVector.length()

    fun Vector.toLuaVector3(): LuaVector3 {
        return LuaVector3(
            this.x,
            this.y,
            this.z
        )
    }

    @TwineNativeProperty("x")
    var xProperty: Double
        get() = bukkitVector.x
        set(value) {
            bukkitVector.x = value
        }

    @TwineNativeProperty("y")
    var yProperty: Double
        get() = bukkitVector.y
        set(value) {
            bukkitVector.y = value
        }

    @TwineNativeProperty("z")
    var zProperty: Double
        get() = bukkitVector.z
        set(value) {
            bukkitVector.z = value
        }

    @TwineNativeFunction
    override fun toString(): String {
        return "vector3($xProperty, $yProperty, $zProperty)"
    }
}