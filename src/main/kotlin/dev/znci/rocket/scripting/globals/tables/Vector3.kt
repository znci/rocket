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
    @TwineOverload
    fun add(vector3: LuaVector3): LuaVector3 {
        bukkitVector.add(vector3.bukkitVector)
        return this
    }

    @TwineNativeFunction
    @TwineOverload
    fun add(x: Double, y: Double, z: Double): LuaVector3 {
        bukkitVector.add(Vector(x, y, z))
        return this
    }

    @TwineNativeFunction
    @TwineOverload
    fun set(vector3: LuaVector3): LuaVector3 {
        bukkitVector = vector3.bukkitVector
        return this
    }

    @TwineNativeFunction
    @TwineOverload
    fun set(x: Double, y: Double, z: Double): LuaVector3 {
        bukkitVector = Vector(x, y, z)
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
    @TwineOverload
    fun divide(comparedVector: LuaVector3): LuaVector3 {
        return bukkitVector.divide(
            comparedVector.bukkitVector
        ).toLuaVector3()
    }

    @TwineNativeFunction
    @TwineOverload
    fun divide(x: Double, y: Double, z: Double): LuaVector3 {
        return bukkitVector.divide(Vector(x, y, z)).toLuaVector3()
    }

    @TwineNativeFunction
    @TwineOverload
    fun div(comparedVector: LuaVector3): LuaVector3 {
        return divide(comparedVector)
    }

    @TwineNativeFunction
    @TwineOverload
    fun div(x: Double, y: Double, z: Double): LuaVector3 {
        return divide(x, y, z)
    }

    @TwineNativeFunction
    @TwineOverload
    fun multiply(comparedVector: LuaVector3): LuaVector3 {
        return bukkitVector.multiply(
            comparedVector.bukkitVector
        ).toLuaVector3()
    }

    @TwineNativeFunction
    @TwineOverload
    fun multiply(x: Double, y: Double, z: Double): LuaVector3 {
        return bukkitVector.multiply(Vector(x, y, z)).toLuaVector3()
    }

    @TwineNativeFunction
    @TwineOverload
    fun mul(comparedVector: LuaVector3): LuaVector3 {
        return multiply(comparedVector)
    }

    @TwineNativeFunction
    @TwineOverload
    fun mul(x: Double, y: Double, z: Double): LuaVector3 {
        return multiply(x, y, z)
    }

    @TwineNativeFunction
    @TwineOverload
    fun subtract(comparedVector: LuaVector3): LuaVector3 {
        return bukkitVector.subtract(
            comparedVector.bukkitVector
        ).toLuaVector3()
    }

    @TwineNativeFunction
    @TwineOverload
    fun subtract(x: Double, y: Double, z: Double): LuaVector3 {
        return bukkitVector.subtract(Vector(x, y, z)).toLuaVector3()
    }

    @TwineNativeFunction
    @TwineOverload
    fun sub(comparedVector: LuaVector3): LuaVector3 {
        return subtract(comparedVector)
    }

    @TwineNativeFunction
    @TwineOverload
    fun sub(x: Double, y: Double, z: Double): LuaVector3 {
        return subtract(x, y, z)
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