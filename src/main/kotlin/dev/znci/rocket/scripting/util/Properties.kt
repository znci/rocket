package dev.znci.rocket.scripting.util

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.ThreeArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

fun defineProperty(
    table: LuaTable,
    propertyName: String,
    getter: () -> LuaValue,
    setter: ((LuaValue) -> Unit)? = null,
    validator: ((LuaValue) -> Boolean)? = null
) {
    val meta = table.getmetatable() ?: LuaTable()

    val indexFunction = meta.get("__index") as? TwoArgFunction
    meta.set("__index", object : TwoArgFunction() {
        override fun call(table: LuaValue, key: LuaValue): LuaValue {
            if (key.tojstring() == propertyName) {
                return getter()
            }
            return indexFunction?.call(table, key) ?: LuaValue.NIL
        }
    })

    val newIndexFunction = meta.get("__newindex") as? ThreeArgFunction
    meta.set("__newindex", object : ThreeArgFunction() {
        override fun call(table: LuaValue, key: LuaValue, value: LuaValue): LuaValue {
            if (key.tojstring() == propertyName) {
                if (setter != null) {
                    if (validator != null && !validator(value)) {
                        error("Invalid input for '$propertyName', got ${value.typename()} (value: ${value.tojstring()})")
                    }
                    setter(value)
                } else {
                    error("Property '$propertyName' is read-only")
                }
            } else {
                newIndexFunction?.call(table, key, value)
            }
            return LuaValue.NONE
        }
    })

    table.setmetatable(meta)
}