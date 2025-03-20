package dev.znci.rocket.scripting.api

import org.luaj.vm2.LuaValue

/**
 * A wrapper class for `LuaValue` that provides additional functionality and type safety.
 * This class extends `LuaValue` and delegates method calls to the wrapped `luaValue` instance.
 */
open class RocketLuaValue(val luaValue: LuaValue = LuaValue.TRUE) : LuaValue() {

    /**
     * Returns the type of the wrapped `LuaValue`.
     *
     * @return The integer type code of the `LuaValue`.
     */
    override fun type(): Int {
        return luaValue.type()
    }

    /**
     * Returns the type name of the wrapped `LuaValue`.
     *
     * @return The name of the Lua type as a string.
     */
    override fun typename(): String? {
        return luaValue.typename()
    }

    companion object {
        /**
         * Represents a `nil` Lua value.
         */
        val NIL = RocketLuaValue(LuaValue.NIL)

        /**
         * Represents a `true` Lua value.
         */
        val TRUE = RocketLuaValue(LuaValue.TRUE)

        /**
         * Represents a `false` Lua value.
         */
        val FALSE = RocketLuaValue(LuaValue.FALSE)

        /**
         * Converts a given Kotlin value to a `RocketLuaValue` instance.
         *
         * @param value The value to be converted.
         * @return A corresponding `RocketLuaValue` representing the input value.
         */
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