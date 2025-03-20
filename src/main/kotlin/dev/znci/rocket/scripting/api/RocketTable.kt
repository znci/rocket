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
package dev.znci.rocket.scripting.api

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ThreeArgFunction
import org.luaj.vm2.lib.TwoArgFunction

// TODO: Move this somewhere else

data class TableSetOptions(
    val getter: (() -> LuaValue)?,
    val setter: ((LuaValue) -> Unit)? = null,
    val validator: ((LuaValue) -> Boolean)? = null
)

open class RocketTable(
    override var valueName: String
): RocketValueBase(valueName) {
    val table: LuaTable = LuaTable()

    fun set(
        propertyName: String,
        options: TableSetOptions
    ) {
        val meta = table.getmetatable() ?: LuaTable()
        val indexFunction = meta.get("__index") as? TwoArgFunction
        val newIndexFunction = meta.get("__newindex") as? ThreeArgFunction

        if (options.getter != null) {
            meta.set("__index", object : TwoArgFunction() {
                override fun call(table: LuaValue, key: LuaValue): LuaValue {
                    if (key.tojstring() == propertyName) {
                        return options.getter()
                    }
                    return indexFunction?.call(table, key) ?: NIL
                }
            })
        }

        meta.set("__newindex", object : ThreeArgFunction() {
            override fun call(table: LuaValue, key: LuaValue, value: LuaValue): LuaValue {
                if (key.tojstring() == propertyName) {
                    if (options.setter != null) {
                        if (options.validator != null && !options.validator(value)) {
                            error("Invalid input for '$propertyName', got ${value.typename()} (value: ${value.tojstring()})")
                        }
                        options.setter(value)
                    } else {
                        return NIL
                    }
                } else {
                    newIndexFunction?.call(table, key, value)
                }
                return NIL
            }
        })

        table.setmetatable(meta)
    }
}
