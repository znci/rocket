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