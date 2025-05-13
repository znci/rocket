/**
 * Copyright 2025 znci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.scripting.annotations.Global
import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import dev.znci.twine.annotations.TwineOverload
import org.bukkit.util.BoundingBox

@Global
class LuaAABBs : TwineNative("aabb") {
    @TwineNativeFunction
    @TwineOverload
    fun new(min: LuaVector3, max: LuaVector3): LuaAABB {
        return LuaAABB(BoundingBox(
            min.xProperty, min.yProperty, min.zProperty,
            max.xProperty, max.yProperty, max.zProperty
        ))
    }

    @TwineNativeFunction
    @TwineOverload
    fun new(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): LuaAABB {
        return LuaAABB(BoundingBox(minX, minY, minZ, maxX, maxY, maxZ))
    }

    @TwineNativeFunction
    fun fromCenterAndSize(center: LuaVector3, size: LuaVector3): LuaAABB {
        val halfSizeX = size.xProperty / 2
        val halfSizeY = size.yProperty / 2
        val halfSizeZ = size.zProperty / 2

        return LuaAABB(BoundingBox(
            center.xProperty - halfSizeX, center.yProperty - halfSizeY, center.zProperty - halfSizeZ,
            center.xProperty + halfSizeX, center.yProperty + halfSizeY, center.zProperty + halfSizeZ
        ))
    }
}

class LuaAABB(
    private var bukkitBox: BoundingBox
) : TwineNative("") {

    @TwineNativeFunction
    fun contains(point: LuaVector3): Boolean {
        return bukkitBox.contains(point.xProperty, point.yProperty, point.zProperty)
    }

    @TwineNativeFunction
    fun contains(x: Double, y: Double, z: Double): Boolean {
        return bukkitBox.contains(x, y, z)
    }

    @TwineNativeFunction
    fun containsAABB(other: LuaAABB): Boolean {
        return bukkitBox.contains(other.bukkitBox)
    }

    @TwineNativeFunction
    fun intersects(other: LuaAABB): Boolean {
        return bukkitBox.overlaps(other.bukkitBox)
    }


    @TwineNativeFunction
    fun expand(amount: Double): LuaAABB {
        bukkitBox.expand(amount)
        return this
    }

    @TwineNativeFunction
    @TwineOverload
    fun expand(x: Double, y: Double, z: Double): LuaAABB {
        bukkitBox.expand(x, y, z)
        return this
    }

    @TwineNativeFunction
    @TwineOverload
    fun expand(direction: LuaVector3, amount: Double): LuaAABB {
        bukkitBox.expand(direction.xProperty, direction.yProperty, direction.zProperty, amount)
        return this
    }

    @TwineNativeFunction
    fun union(other: LuaAABB): LuaAABB {
        bukkitBox.union(other.bukkitBox)
        return this
    }

    @TwineNativeFunction
    fun expandToContainAABB(other: LuaAABB): LuaAABB {
        return union(other)
    }

    @TwineNativeFunction
    fun shift(x: Double, y: Double, z: Double): LuaAABB {
        bukkitBox.shift(x, y, z)
        return this
    }

    @TwineNativeFunction
    @TwineOverload
    fun shift(offset: LuaVector3): LuaAABB {
        bukkitBox.shift(offset.xProperty, offset.yProperty, offset.zProperty)
        return this
    }

    @TwineNativeFunction
    fun rayTrace(start: LuaVector3, direction: LuaVector3, maxDistance: Double): LuaVector3? {
        val dir = direction.clone().normalize()
        val result = bukkitBox.rayTrace(
            start.toBukkitVector(),
            dir.toBukkitVector(),
            maxDistance
        ) ?: return null
        return result.hitPosition.toLuaVector3()
    }

    @TwineNativeFunction
    fun intersection(other: LuaAABB): LuaAABB {
        return LuaAABB(bukkitBox.intersection(other.bukkitBox))
    }

    @TwineNativeFunction
    fun closestPointTo(point: LuaVector3): LuaVector3 {
        return LuaVector3(
            point.xProperty.coerceIn(bukkitBox.minX, bukkitBox.maxX),
            point.yProperty.coerceIn(bukkitBox.minY, bukkitBox.maxY),
            point.zProperty.coerceIn(bukkitBox.minZ, bukkitBox.maxZ)
        )
    }

    @TwineNativeFunction
    fun distanceTo(point: LuaVector3): Double {
        return closestPointTo(point).distance(point)
    }

    @TwineNativeProperty("center")
    val center: LuaVector3
        get() = LuaVector3(
            bukkitBox.centerX,
            bukkitBox.centerY,
            bukkitBox.centerZ
        )

    @TwineNativeProperty("volume")
    val volume: Double
        get() = bukkitBox.volume

    @TwineNativeProperty("size")
    val size: LuaVector3
        get() = LuaVector3(bukkitBox.widthX,  bukkitBox.height, bukkitBox.widthZ)

    @TwineNativeProperty("min")
    val minProperty: LuaVector3
        get() = bukkitBox.min.toLuaVector3()

    @TwineNativeProperty("max")
    val maxProperty: LuaVector3
        get() = maxProperty

    @TwineNativeFunction
    override fun toString(): String {
        return "AABB($minProperty, $maxProperty)"
    }
}