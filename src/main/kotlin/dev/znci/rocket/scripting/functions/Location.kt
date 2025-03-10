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
                // get first world, could be replaced with getting the world specified in server.properties, I don't know if this does that
                val world = if (args.narg() >= 4) args.arg(4).checkjstring() else Bukkit.getWorlds()[0].name
                val yaw = if (args.narg() >= 5) args.arg(5).tofloat() else 0f
                val pitch = if (args.narg() >= 6) args.arg(6).tofloat() else 0f
                return LuaLocation(x, y, z, world, yaw, pitch)
            }
        })
    }
}

class LuaLocation(x: Double, y: Double, z: Double, worldName: String, yaw: Float, pitch: Float) : LuaTable() {
    private var world: World? = Bukkit.getWorld(worldName)
    private var location: Location = Location(world, x, y, z, yaw, pitch)

    companion object {
        fun fromBukkit(location: Location): LuaLocation {
            return LuaLocation(location.x, location.y, location.z, location.world.name, location.yaw, location.pitch)
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
                        "world" -> LuaValue.valueOf(location.world.name)
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
                location = Location(newWorld, location.x, location.y, location.z, location.yaw, location.pitch)
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

    fun toBukkit(): Location? {
        val world = Bukkit.getWorld(get("world").tojstring()) ?: return null
        val x = get("x").todouble()
        val y = get("y").todouble()
        val z = get("z").todouble()
        val yaw = get("yaw").tofloat()
        val pitch = get("pitch").tofloat()
        return Location(world, x, y, z, yaw, pitch)
    }
}