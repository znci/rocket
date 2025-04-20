package dev.znci.rocket.scripting.classes

import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import org.bukkit.util.Vector

class LuaVector(val vector: Vector) : TwineNative("") {
    constructor(x: Double, y: Double, z: Double) : this(Vector(x, y, z))

    @TwineNativeProperty
    val blockX
        get() = vector.blockX

    @TwineNativeProperty
    val blockY
        get() = vector.blockY

    @TwineNativeProperty
    val blockZ
        get() = vector.blockZ

    @TwineNativeProperty
    var x
        get() = vector.x
        set(value) { vector.x = value }

    @TwineNativeProperty
    var y
        get() = vector.y
        set(value) { vector.y = value }

    @TwineNativeProperty
    var z
        get() = vector.z
        set(value) { vector.z = value }

    @TwineNativeFunction
    fun add(vector: LuaVector): LuaVector {
        this.vector.add(vector.vector)
        return this
    }

    @TwineNativeFunction
    fun angle(vector: LuaVector) = this.vector.angle(vector.vector)

    @TwineNativeFunction
    fun checkFinite() = this.vector.checkFinite()

    @TwineNativeFunction
    fun clone() = LuaVector(this.vector.clone())

    @TwineNativeFunction
    fun copy(vector: LuaVector): LuaVector {
        this.vector.copy(vector.vector)
        return this
    }

    @TwineNativeFunction
    fun crossProduct(vector: LuaVector): LuaVector {
        this.vector.crossProduct(vector.vector)
        return this
    }

    @TwineNativeFunction
    fun distance(vector: LuaVector) = this.vector.distance(vector.vector)

    @TwineNativeFunction
    fun distanceSquared(vector: LuaVector) = this.vector.distanceSquared(vector.vector)

    @TwineNativeFunction
    fun divide(vector: LuaVector): LuaVector {
        this.vector.divide(vector.vector)
        return this
    }

    @TwineNativeFunction
    fun dot(vector: LuaVector) = this.vector.dot(vector.vector)

    @TwineNativeFunction
    fun getCrossProduct(vector: LuaVector) = this.vector.getCrossProduct(vector.vector)

    @TwineNativeFunction
    fun getMaximum(vector: LuaVector): LuaVector {
        this.vector.copy(Vector.getMaximum(this.vector, vector.vector))
        return this
    }

    @TwineNativeFunction
    fun getMinimum(vector: LuaVector): LuaVector {
        this.vector.copy(Vector.getMinimum(this.vector, vector.vector))
        return this
    }

    @TwineNativeFunction
    fun getMidpoint(vector: LuaVector): LuaVector {
        this.vector.copy(this.vector.midpoint(vector.vector))
        return this
    }

    @TwineNativeFunction
    fun inAxisAlignedBoundingBox(min: LuaVector, max: LuaVector) = this.vector.isInAABB(min.vector, max.vector)

    @TwineNativeFunction
    fun inSphere(center: LuaVector, radius: Double) = this.vector.isInSphere(center.vector, radius)

    @TwineNativeFunction
    fun isNormalized() = this.vector.isNormalized

    @TwineNativeFunction
    fun isZero() = this.vector.isZero

    @TwineNativeFunction
    fun magnitude() = this.vector.length()

    @TwineNativeFunction
    fun magnitudeSquared() = this.vector.lengthSquared()

    @TwineNativeFunction
    fun multiply(scalar: Double): LuaVector {
        this.vector.multiply(scalar)
        return this
    }

    @TwineNativeFunction
    fun normalize(): LuaVector {
        this.vector.normalize()
        return this
    }

    @TwineNativeFunction
    fun rotateAroundAxis(axis: LuaVector, angle: Double): LuaVector {
        this.vector.rotateAroundAxis(axis.vector, angle)
        return this
    }

    @TwineNativeFunction
    fun rotateAroundNonUnitAxis(axis: LuaVector, angle: Double): LuaVector {
        this.vector.rotateAroundNonUnitAxis(axis.vector, angle)
        return this
    }

    @TwineNativeFunction
    fun rotateAroundX(angle: Double): LuaVector {
        this.vector.rotateAroundX(angle)
        return this
    }

    @TwineNativeFunction
    fun rotateAroundY(angle: Double): LuaVector {
        this.vector.rotateAroundY(angle)
        return this
    }

    @TwineNativeFunction
    fun rotateAroundZ(angle: Double): LuaVector {
        this.vector.rotateAroundZ(angle)
        return this
    }

    @TwineNativeFunction
    fun zero(): LuaVector {
        this.vector.zero()
        return this
    }
}