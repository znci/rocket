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

import dev.znci.rocket.scripting.api.RocketNative
import dev.znci.rocket.scripting.api.RocketTable
import dev.znci.rocket.scripting.api.annotations.RocketNativeFunction
import dev.znci.rocket.scripting.api.annotations.RocketNativeProperty
import dev.znci.rocket.scripting.util.getWorldByNameOrUUID
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import java.util.*

class LuaLocations : RocketNative("location") {
    @RocketNativeFunction
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
) : RocketNative("") {
    private var world: World? = getWorldByNameOrUUID(worldUUID)
    private var location: Location = Location(world, x, y, z, yaw, pitch)

    @RocketNativeProperty("x")
    var xProperty: Double
        get() = location.x
        set(value) {
            location.x = value
        }

    @RocketNativeProperty("y")
    var yProperty: Double
        get() = location.y
        set(value) {
            location.y = value
        }

    @RocketNativeProperty("z")
    var zProperty: Double
        get() = location.z
        set(value) {
            location.z = value
        }

    @RocketNativeProperty("world")
    var worldProperty: String
        get() = location.world.name
        set(value) {
            location.world = getWorldByNameOrUUID(value)
        }

    @RocketNativeProperty("worldUUID")
    var worldUUIDProperty: String
        get() = location.world.uid.toString()
        set(value) {
            location.world = getWorldByNameOrUUID(value)
        }

    @RocketNativeProperty("yaw")
    var yawProperty: Float
        get() = location.yaw
        set(value) {
            location.yaw = value
        }

    @RocketNativeProperty("pitch")
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
        fun fromBukkit(location: Location): RocketTable {
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
}


//
//class LuaLocation(
//    x: Double,
//    y: Double,
//    z: Double,
//    worldUUID: String,
//    yaw: Float = 0f,
//    pitch: Float = 0f
//) : LuaTable() {
//    private var world: World? = Bukkit.getWorld(UUID.fromString(worldUUID))
//    private var location: Location = Location(world, x, y, z, yaw, pitch)
//
//    companion object {
//        fun fromBukkit(location: Location): LuaTable {
//            return LuaLocation(
//                location.x,
//                location.y,
//                location.z,
//                location.world.uid.toString(),
//                location.yaw,
//                location.pitch
//            ).getLocationTable()
//        }
//    }
//
//    fun getLocationTable(): LuaTable {
//        val table = LuaTable()
//
//        defineProperty(table, "x", { valueOf(location.x) }, { value -> location.x = value.todouble() })
//        defineProperty(table, "y", { valueOf(location.y) }, { value -> location.y = value.todouble() })
//        defineProperty(table, "z", { valueOf(location.z) }, { value -> location.z = value.todouble() })
//        defineProperty(table, "world", { valueOf(location.world.name) }, { value -> location.world = Bukkit.getWorld(UUID.fromString(value.tojstring())) })
//        defineProperty(table, "worldUUID", { valueOf(location.world.uid.toString()) }, { value -> location.world = Bukkit.getWorld(UUID.fromString(value.tojstring())) })
//        defineProperty(table, "yaw", { valueOf(location.yaw.toDouble()) }, { value -> location.yaw = value.tofloat() })
//        defineProperty(table, "pitch", { valueOf(location.pitch.toDouble()) }, { value -> location.pitch = value.tofloat() })
//
//        return table
//    }
//}

fun LuaValue.toBukkitLocation(): Location {
    if (this !is LuaTable) {
        error("Expected a LuaTable, got ${this.typename()} (value: ${this.tojstring()})")
    }

    return try {
        val x = this.get("x").todouble()
        val y = this.get("y").todouble()
        val z = this.get("z").todouble()
        val worldUUIDStr = this.get("worldUUID").tojstring()
        val worldUUID = try {
            UUID.fromString(worldUUIDStr)
        } catch (e: IllegalArgumentException) {
            error("Invalid 'worldUUID': Not a valid UUID (value: $worldUUIDStr)")
        }
        val world = Bukkit.getWorld(worldUUID)
        val yaw = this.get("yaw").tofloat()
        val pitch = this.get("pitch").tofloat()
        Location(world, x, y, z, yaw, pitch)
    } catch (e: Exception) {
        error("LuaTable does not represent a valid location: ${e.message}")
    }
}