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

/**
 * Data class that provides the options for setting properties on a `LuaTable`.
 * It provides functionality for defining a getter, setter, and validator for properties in a table.
 *
 * @param getter The getter function for retrieving the property's value. It returns a `LuaValue` and can be `null`.
 * @param setter The setter function for setting the property's value. It takes a `LuaValue` and can be `null`.
 * @param validator The validator function that checks the validity of the value before setting it. It returns a `Boolean` and can be `null`.
 */
data class TableSetOptions(
    val getter: (() -> LuaValue)?,
    val setter: ((LuaValue) -> Unit)? = null,
    val validator: ((LuaValue) -> Boolean)? = null
)

/**
 * Represents a table, which is a wrapper around a `LuaTable`.
 * This class provides functionality for managing Lua properties and exposing them with custom getter, setter, and validation logic.
 * It allows you to set properties dynamically on the table, with custom logic for getting, setting, and validating property values.
 *
 * @param valueName The name of the table, used for specifying the name of the table if it becomes a global.
 */
open class RocketTable(
    override var valueName: String
): RocketValueBase(valueName) {
    /**
     * The internal `LuaTable` instance that holds the table's data.
     */
    val table: LuaTable = LuaTable()

    /**
     * Sets a property on the table with custom getter, setter, and validator options.
     *
     * @param propertyName The name of the property to set on the table.
     * @param options The options that define how the property should behave, including getter, setter, and validator.
     */
    fun set(
        propertyName: String,
        options: TableSetOptions
    ) {
        val meta = table.getmetatable() ?: LuaTable()
        val indexFunction = meta.get("__index") as? TwoArgFunction
        val newIndexFunction = meta.get("__newindex") as? ThreeArgFunction

        // Define the getter
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

        // Define the setter
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
