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
package dev.znci.rocket.scripting.functions

import dev.znci.rocket.interfaces.Storable
import dev.znci.rocket.scripting.util.defineProperty
import dev.znci.rocket.scripting.util.getWorldByNameOrUUID
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.json.JSONObject
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.VarArgFunction
import java.util.*

class LuaLocations : LuaTable() {
    init {
        set("new", object : VarArgFunction() {
            override fun invoke(args: Varargs): LuaValue {
                if (args.narg() < 3) return LuaValue.FALSE
                val x = args.arg(1).todouble()
                val y = args.arg(2).todouble()
                val z = args.arg(3).todouble()
                val worldUUID = if (args.narg() >= 4) {
                    val worldNameOrUUID = args.arg(4).checkjstring()
                    getWorldByNameOrUUID(worldNameOrUUID).uid.toString()
                } else {
                    Bukkit.getWorlds().first().uid.toString()
                }
                val yaw = if (args.narg() >= 5) args.arg(5).tofloat() else 0f
                val pitch = if (args.narg() >= 6) args.arg(6).tofloat() else 0f
                return LuaLocation(x, y, z, worldUUID, yaw, pitch).getLocationTable()
            }
        })
    }
}

class LuaLocation(
    x: Double,
    y: Double,
    z: Double,
    worldUUID: String,
    yaw: Float = 0f,
    pitch: Float = 0f
) : LuaTable(), Storable {
    private var world: World? = Bukkit.getWorld(UUID.fromString(worldUUID))
    private var location: Location = Location(world, x, y, z, yaw, pitch)

    companion object {
        fun fromBukkit(location: Location): LuaTable {
            return LuaLocation(
                location.x,
                location.y,
                location.z,
                location.world.uid.toString(),
                location.yaw,
                location.pitch
            ).getLocationTable()
        }

        @Suppress("unused") // Used by Storable
        fun fromJson(json: String): LuaLocation {
            val obj = JSONObject(json)
            return LuaLocation(
                obj.getDouble("x"),
                obj.getDouble("y"),
                obj.getDouble("z"),
                obj.getString("worldUUID"),
                obj.getDouble("yaw").toFloat(),
                obj.getDouble("pitch").toFloat()
            )
        }
    }

    override fun toJson(): String {
        val obj = JSONObject()
        obj.put("x", location.x)
        obj.put("y", location.y)
        obj.put("z", location.z)
        obj.put("worldUUID", location.world.uid.toString())
        obj.put("yaw", location.yaw)
        obj.put("pitch", location.pitch)
        return obj.toString()
    }

    fun getLocationTable(): LuaTable {
        val table = LuaTable()

        defineProperty(table, "x", { LuaValue.valueOf(location.x) }, { value -> location.x = value.todouble() })
        defineProperty(table, "y", { LuaValue.valueOf(location.y) }, { value -> location.y = value.todouble() })
        defineProperty(table, "z", { LuaValue.valueOf(location.z) }, { value -> location.z = value.todouble() })
        defineProperty(table, "world", { LuaValue.valueOf(location.world.name) }, { value -> location.world = Bukkit.getWorld(UUID.fromString(value.tojstring())) })
        defineProperty(table, "worldUUID", { LuaValue.valueOf(location.world.uid.toString()) }, { value -> location.world = Bukkit.getWorld(UUID.fromString(value.tojstring())) })
        defineProperty(table, "yaw", { LuaValue.valueOf(location.yaw.toDouble()) }, { value -> location.yaw = value.tofloat() })
        defineProperty(table, "pitch", { LuaValue.valueOf(location.pitch.toDouble()) }, { value -> location.pitch = value.tofloat() })

        return table
    }
}

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