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

    companion object {
        fun fromBukkit(location: Location): LuaLocation {
            return LuaLocation(location.x, location.y, location.z, location.world.name)
        }
    }
}