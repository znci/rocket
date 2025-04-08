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
import org.luaj.vm2.lib.ThreeArgFunction
import org.luaj.vm2.lib.TwoArgFunction

/**
 * Defines a property on a Lua table with a getter, setter, and optional validator.
 * This function allows you to define custom properties that can be accessed and mutated using Lua table indexing.
 * If a setter is provided, it ensures that the setter validates the value using the optional validator before setting it.
 * If the property is read-only, an error is thrown if an attempt is made to set it.
 *
 * @param table The Lua table on which the property will be defined.
 * @param propertyName The name of the property to define.
 * @param getter The function to retrieve the value of the property.
 * @param setter An optional function to set the value of the property. If null, the property is read-only.
 * @param validator An optional function to validate the value before setting it. Returns `true` if valid, `false` if invalid.
 * @throws org.luaj.vm2.LuaError If the value provided to the setter is invalid according to the validator.
 */

@Deprecated("RocketNative Kotlin should be used instead.", ReplaceWith("RocketNative"), DeprecationLevel.WARNING)
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
            return indexFunction?.call(table, key) ?: NIL
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
            return NONE
        }
    })

    table.setmetatable(meta)
}