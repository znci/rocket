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
import dev.znci.rocket.scripting.api.RocketError
import dev.znci.rocket.scripting.util.getWorldByNameOrUUID
import dev.znci.twine.nativex.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.luaj.vm2.LuaValue
import java.util.*

@Global
class LuaLocations : TwineNative("location") {
    @TwineNativeFunction
    fun new(x: Double, y: Double, z: Double, worldUUID: String, yaw: Float = 0f, pitch: Float = 0f): LuaLocation {
        return LuaLocation(x, y, z, worldUUID, yaw, pitch)
    }
}

@Suppress("unused")
class LuaLocation(
    x: Double,
    y: Double,
    z: Double,
    worldUUID: String,
    yaw: Float = 0f,
    pitch: Float = 0f
) : TwineNative("") {
    private var world: World? = getWorldByNameOrUUID(worldUUID)
    private var location: Location = Location(world, x, y, z, yaw, pitch)

    @TwineNativeProperty("x")
    var xProperty: Double
        get() = location.x
        set(value) {
            location.x = value
        }

    @TwineNativeProperty("y")
    var yProperty: Double
        get() = location.y
        set(value) {
            location.y = value
        }

    @TwineNativeProperty("z")
    var zProperty: Double
        get() = location.z
        set(value) {
            location.z = value
        }

    @TwineNativeProperty("world")
    var worldProperty: String
        get() = location.world.name
        set(value) {
            location.world = getWorldByNameOrUUID(value)
        }

    @TwineNativeProperty("worldUUID")
    var worldUUIDProperty: String
        get() = location.world.uid.toString()
        set(value) {
            location.world = getWorldByNameOrUUID(value)
        }

    @TwineNativeProperty("yaw")
    var yawProperty: Float
        get() = location.yaw
        set(value) {
            location.yaw = value
        }

    @TwineNativeProperty("pitch")
    var pitchProperty: Float
        get() = location.pitch
        set(value) {
            location.pitch = value
        }

    override fun get(name: LuaValue): LuaValue {
        return when (name.tojstring()) {
            "x" -> LuaValue.valueOf(location.x)
            "y" -> LuaValue.valueOf(location.y)
            "z" -> LuaValue.valueOf(location.z)
            "world" -> LuaValue.valueOf(location.world.name)
            "worldUUID" -> LuaValue.valueOf(location.world.uid.toString())
            "yaw" -> LuaValue.valueOf(location.yaw.toDouble())
            "pitch" -> LuaValue.valueOf(location.pitch.toDouble())
            else -> super.get(name)
        }
    }

    companion object {
        fun fromBukkit(location: Location): LuaLocation {
            return LuaLocation(
                location.x,
                location.y,
                location.z,
                location.world.uid.toString(),
                location.yaw,
                location.pitch
            )
        }
    }

    fun toBukkit(): Location {
        return try {
            val x = this.xProperty
            val y = this.yProperty
            val z = this.zProperty
            val worldUUIDStr = this.worldUUIDProperty

            val worldUUID = try {
                UUID.fromString(worldUUIDStr)
            } catch (e: IllegalArgumentException) {
                Bukkit.getWorld("world")?.uid ?: throw RocketError("Default world \"world\" does not exist.")
            }

            val world = Bukkit.getWorld(worldUUID)

            val yaw = this.yawProperty
            val pitch = this.pitchProperty

            Location(world, x, y, z, yaw, pitch)
        } catch (e: Exception) {
            throw RocketError("Invalid location provided.")
        }
    }
}