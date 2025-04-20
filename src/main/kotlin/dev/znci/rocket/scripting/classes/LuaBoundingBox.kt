package dev.znci.rocket.scripting.classes

import dev.znci.rocket.scripting.globals.tables.LuaLocation
import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import org.bukkit.util.BoundingBox

@Suppress("unused")
class LuaBoundingBox(val boundingBox: BoundingBox) : TwineNative("") {
    constructor(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double) : this(
        BoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
    )

    @TwineNativeProperty
    val center
        get() = LuaVector(boundingBox.center)

    @TwineNativeProperty
    val centerX
        get() = boundingBox.centerX

    @TwineNativeProperty
    val centerY
        get() = boundingBox.centerY

    @TwineNativeProperty
    val centerZ
        get() = boundingBox.centerZ

    @TwineNativeProperty
    val max
        get() = LuaVector(boundingBox.max)

    @TwineNativeProperty
    val maxX
        get() = boundingBox.maxX

    @TwineNativeProperty
    val maxY
        get() = boundingBox.maxY

    @TwineNativeProperty
    val maxZ
        get() = boundingBox.maxZ

    @TwineNativeProperty
    val min
        get() = LuaVector(boundingBox.min)

    @TwineNativeProperty
    val minX
        get() = boundingBox.minX

    @TwineNativeProperty
    val minY
        get() = boundingBox.minY

    @TwineNativeProperty
    val minZ
        get() = boundingBox.minZ

    @TwineNativeProperty
    val volume
        get() = boundingBox.volume

    @TwineNativeProperty
    val widthX
        get() = boundingBox.widthX

    @TwineNativeProperty
    val widthZ
        get() = boundingBox.widthZ

    @TwineNativeFunction
    fun clone() = LuaBoundingBox(boundingBox.clone())

    @TwineNativeFunction
    fun containsCoordinates(x: Double, y: Double, z: Double) = boundingBox.contains(x, y, z)

    @TwineNativeFunction
    fun containsBoundingBox(boundingBox: LuaBoundingBox) = this.boundingBox.contains(boundingBox.boundingBox)

    @TwineNativeFunction
    fun containsVector(vector: LuaVector) = boundingBox.contains(vector.vector)

    @TwineNativeFunction
    fun containsVectorCorners(vectorMin: LuaVector, vectorMax: LuaVector) =
        boundingBox.contains(vectorMin.vector, vectorMax.vector)

    @TwineNativeFunction
    fun copy(boundingBox: LuaBoundingBox): LuaBoundingBox {
        this.boundingBox.copy(boundingBox.boundingBox)
        return this
    }

    @TwineNativeFunction
    fun expandAllDirections(amount: Double): LuaBoundingBox {
        this.boundingBox.expand(amount)
        return this
    }

    @TwineNativeFunction
    fun expandInPositiveNegativeDirections(x: Double, y: Double, z: Double): LuaBoundingBox {
        this.boundingBox.expand(x, y, z)
        return this
    }

    @TwineNativeFunction
    fun expandInSpecifiedDirection(x: Double, y: Double, z: Double, expansion: Double): LuaBoundingBox {
        this.boundingBox.expand(x, y, z, expansion)
        return this
    }

    @TwineNativeFunction
    fun expandByCorrespondingDirections(x: Double, y: Double, z: Double): LuaBoundingBox {
        this.boundingBox.expand(x, y, z)
        return this
    }

    @TwineNativeFunction
    fun expandByVector(vector: LuaVector): LuaBoundingBox {
        this.boundingBox.expand(vector.vector)
        return this
    }

    @TwineNativeFunction
    fun expandByVectorInDirection(vector: LuaVector, expansion: Double): LuaBoundingBox {
        this.boundingBox.expand(vector.vector, expansion)
        return this
    }

    @TwineNativeFunction
    fun expandDirectionalCoordinates(x: Double, y: Double, z: Double): LuaBoundingBox {
        this.boundingBox.expandDirectional(x, y, z)
        return this
    }

    @TwineNativeFunction
    fun expandDirectionalVector(vector: LuaVector): LuaBoundingBox {
        this.boundingBox.expandDirectional(vector.vector)
        return this
    }

    @TwineNativeFunction
    fun intersection(boundingBox: LuaBoundingBox): LuaBoundingBox {
        this.boundingBox.intersection(boundingBox.boundingBox)
        return this
    }

    @TwineNativeFunction
    fun overlaps(boundingBox: LuaBoundingBox) = this.boundingBox.overlaps(boundingBox.boundingBox)

    @TwineNativeFunction
    fun overlapsVector(vectorMin: LuaVector, vectorMax: LuaVector) = this.boundingBox.overlaps(vectorMin.vector, vectorMax.vector)

    @TwineNativeFunction
    fun rayTrace(start: LuaVector, direction: LuaVector, maxDistance: Double) = this.boundingBox.rayTrace(start.vector, direction.vector, maxDistance)?.let { LuaRayTraceResult(it) }

    @TwineNativeFunction
    fun resize(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): LuaBoundingBox {
        this.boundingBox.resize(minX, minY, minZ, maxX, maxY, maxZ)
        return this
    }

    @TwineNativeFunction
    fun shiftCoordinates(x: Double, y: Double, z: Double): LuaBoundingBox {
        this.boundingBox.shift(x, y, z)
        return this
    }

    @TwineNativeFunction
    fun shiftLocation(location: LuaLocation): LuaBoundingBox {
        this.boundingBox.shift(location.toBukkitLocation())
        return this
    }

    @TwineNativeFunction
    fun shiftVector(vector: LuaVector): LuaBoundingBox {
        this.boundingBox.shift(vector.vector)
        return this
    }

    @TwineNativeFunction
    fun unionCoordinates(x: Double, y: Double, z: Double): LuaBoundingBox {
        this.boundingBox.union(x, y, z)
        return this
    }

    @TwineNativeFunction
    fun unionLocation(location: LuaLocation): LuaBoundingBox {
        this.boundingBox.union(location.toBukkitLocation())
        return this
    }

    @TwineNativeFunction
    fun unionBoundingBox(boundingBox: LuaBoundingBox): LuaBoundingBox {
        this.boundingBox.union(boundingBox.boundingBox)
        return this
    }

    @TwineNativeFunction
    fun unionVector(vector: LuaVector): LuaBoundingBox {
        this.boundingBox.union(vector.vector)
        return this
    }
}