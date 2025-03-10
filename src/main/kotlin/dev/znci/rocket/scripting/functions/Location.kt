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

import dev.znci.rocket.scripting.util.defineProperty
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
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
                    Bukkit.getWorld(args.arg(4).checkjstring())?.uid.toString()
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

class LuaLocation(x: Double, y: Double, z: Double, worldUUID: String, yaw: Float = 0f, pitch: Float = 0f) : LuaTable() {
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
    }
    fun getLocationTable(): LuaTable {
        val table = LuaTable()
        defineProperty(table, "x", { LuaValue.valueOf(location.x) })
        defineProperty(table, "y", { LuaValue.valueOf(location.y) })
        defineProperty(table, "z", { LuaValue.valueOf(location.z) })
        defineProperty(table, "world", { LuaValue.valueOf(location.world.name) })
        defineProperty(table, "worldUUID", { LuaValue.valueOf(location.world.uid.toString()) })
        defineProperty(table, "yaw", { LuaValue.valueOf(location.yaw.toDouble()) })
        defineProperty(table, "pitch", { LuaValue.valueOf(location.pitch.toDouble()) })
        return table
    }
    fun toBukkit(): Location {
        return location.clone()
    }
}