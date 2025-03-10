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

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.VarArgFunction
import java.util.*

class LuaLocations : LuaTable() {
    init {
        set("new", object : VarArgFunction() {
            override fun invoke(args: Varargs): LuaValue {
                if (args.narg() < 3) {
                    return LuaValue.FALSE
                }
                val x = args.arg(1).todouble()
                val y = args.arg(2).todouble()
                val z = args.arg(3).todouble()

                val worldArg = if (args.narg() >= 4) args.arg(4).checkjstring() else null
                val world = resolveWorld(worldArg)

                val yaw = if (args.narg() >= 5) args.arg(5).tofloat() else 0f
                val pitch = if (args.narg() >= 6) args.arg(6).tofloat() else 0f

                return LuaLocation(x, y, z, world.uid.toString(), yaw, pitch)
            }
        })
    }

    private fun resolveWorld(identifier: String?): World {
        if (identifier.isNullOrBlank()) {
            return Bukkit.getWorlds().firstOrNull() ?: throw IllegalStateException("No worlds are loaded on the server!")
        }

        val worldByName = Bukkit.getWorld(identifier)
        if (worldByName != null) return worldByName

        return try {
            val uuid = UUID.fromString(identifier)
            Bukkit.getWorld(uuid) ?: throw IllegalArgumentException("World with UUID '$uuid' not found!")
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid world identifier: '$identifier'")
        }
    }
}

class LuaLocation(
    x: Double,
    y: Double,
    z: Double,
    worldUID: String,
    yaw: Float = 0f,
    pitch: Float = 0f
) : LuaTable() {
    private var world: World? = Bukkit.getWorld(UUID.fromString(worldUID))
    private var location: Location = Location(world, x, y, z, yaw, pitch)

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

    init {
        val metaTable = LuaTable().apply {
            set("__index", object : TwoArgFunction() {
                override fun call(table: LuaValue, key: LuaValue): LuaValue {
                    return when (key.tojstring()) {
                        "x" -> LuaValue.valueOf(location.x)
                        "y" -> LuaValue.valueOf(location.y)
                        "z" -> LuaValue.valueOf(location.z)
                        // eventually we would want a World class here
                        "world" -> LuaValue.valueOf(location.world.name)
                        "worldUUID" -> LuaValue.valueOf(worldUID)
                        "yaw" -> LuaValue.valueOf(location.yaw.toDouble())
                        "pitch" -> LuaValue.valueOf(location.pitch.toDouble())
                        else -> LuaValue.NIL
                    }
                }
            })
        }

        this.setmetatable(metaTable)

        set("setX", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                location.x = value.todouble()
                return value
            }
        })

        set("setY", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                location.y = value.todouble()
                return value
            }
        })

        set("setZ", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                location.z = value.todouble()
                return value
            }
        })

        set("setWorld", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                val worldArg = value.checkjstring()
                val newWorld = Bukkit.getWorld(worldArg) ?: return LuaValue.FALSE
                location.world = newWorld
                return LuaValue.valueOf(newWorld.name)
            }
        })

        set("setYaw", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                location.yaw = value.tofloat()
                return LuaValue.valueOf(location.yaw.toDouble())
            }
        })

        set("setPitch", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                location.pitch = value.tofloat()
                return LuaValue.valueOf(location.pitch.toDouble())
            }
        })
    }

    fun toBukkit(): Location {
        return location.clone()
    }
}