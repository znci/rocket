package dev.znci.rocket.scripting.api

import org.luaj.vm2.LuaValue

class RocketLuaValue() {
    companion object {
        val NIL = LuaValue.NIL
        val TRUE = LuaValue.TRUE
        val FALSE = LuaValue.FALSE

        fun valueOf(value: Any?): LuaValue {
            return when (value) {
                is String -> LuaValue.valueOf(value)
                is Boolean -> LuaValue.valueOf(value)
                is Int -> LuaValue.valueOf(value)
                is Double -> LuaValue.valueOf(value)
                else -> {
                    LuaValue.NIL
                }
            } as LuaValue
        }
    }
}