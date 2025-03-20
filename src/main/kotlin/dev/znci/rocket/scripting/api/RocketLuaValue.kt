package dev.znci.rocket.scripting.api

import org.luaj.vm2.LuaValue

open class RocketLuaValue(val luaValue: LuaValue = LuaValue.TRUE) : LuaValue() {
    override fun type(): Int {
        return luaValue.type()
    }

    override fun typename(): String? {
        return luaValue.typename()
    }

    companion object {
        val NIL = RocketLuaValue(LuaValue.NIL)
        val TRUE = RocketLuaValue(LuaValue.TRUE)
        val FALSE = RocketLuaValue(LuaValue.FALSE)

        fun valueOf(value: Any?): RocketLuaValue {
            return when (value) {
                is String -> RocketLuaValue(LuaValue.valueOf(value))
                is Boolean -> RocketLuaValue(LuaValue.valueOf(value))
                is Int -> RocketLuaValue(LuaValue.valueOf(value))
                is Double -> RocketLuaValue(LuaValue.valueOf(value))
                is LuaValue -> RocketLuaValue(value)
                else -> NIL
            }
        }
    }

    override fun toString(): String = luaValue.toString()
}