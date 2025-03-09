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
import org.luaj.vm2.lib.VarArgFunction

class LuaLocations : LuaTable() {
    init {
        set("new", object : VarArgFunction() {
            override fun invoke(args: Varargs): LuaValue {
                if (args.narg() < 4) {
                    return LuaValue.FALSE
                }
                val x = args.arg(1).todouble()
                val y = args.arg(2).todouble()
                val z = args.arg(3).todouble()
                val worldName = args.arg(4).checkjstring()
                return LuaLocation(x, y, z, worldName)
            }
        })
    }
}

class LuaLocation(x: Double, y: Double, z: Double, worldName: String) : LuaTable() {
    private var world: World? = Bukkit.getWorld(worldName)
    private var location: Location? = world?.let { Location(it, x, y, z) }

    init {
        set("x", LuaValue.valueOf(x))
        set("y", LuaValue.valueOf(y))
        set("z", LuaValue.valueOf(z))
        set("world", LuaValue.valueOf(worldName))

        set("setX", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                val newX = value.todouble()
                location?.x = newX
                set("x", LuaValue.valueOf(newX))
                return LuaValue.valueOf(newX)
            }
        })

        set("setY", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                val newY = value.todouble()
                location?.y = newY
                set("y", LuaValue.valueOf(newY))
                return LuaValue.valueOf(newY)
            }
        })

        set("setZ", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                val newZ = value.todouble()
                location?.z = newZ
                set("z", LuaValue.valueOf(newZ))
                return LuaValue.valueOf(newZ)
            }
        })

        set("setWorld", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                val worldArg = value.checkjstring()
                val newWorld = Bukkit.getWorld(worldArg) ?: return LuaValue.FALSE
                location = Location(newWorld, location?.x ?: 0.0, location?.y ?: 0.0, location?.z ?: 0.0)
                set("world", value)
                return LuaValue.valueOf(newWorld.name)
            }
        })
    }

    fun toBukkit(): Location? {
        val world = Bukkit.getWorld(get("world").tojstring()) ?: return null
        val x = get("x").todouble()
        val y = get("y").todouble()
        val z = get("z").todouble()
        return Location(world, x, y, z)
    }

    companion object {
        fun fromBukkit(location: Location): LuaLocation {
            return LuaLocation(location.x, location.y, location.z, location.world.name)
        }
    }
}