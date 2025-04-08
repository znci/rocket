package dev.znci.rocket.scripting.api

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import kotlin.reflect.KClass

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

class RocketEnum(name: String) : RocketTable(name) {
    fun toLuaTable(enum: Enum<*>): LuaTable {
        val table = this.table
        for (enumConstant in enum.javaClass.enumConstants) {
            table.set(enumConstant.name, RocketLuaValue(LuaValue.valueOf(enumConstant.ordinal)))
        }
        return table
    }

    fun fromLuaTable(luaTable: LuaTable, enum: KClass<out Any>): Enum<*> {
        val enumConstants = enum.java.enumConstants
        for (i in 0 until luaTable.length()) {
            val name = luaTable[i + 1].toString()
            for (enumConstant in enumConstants as Array<Enum<*>>) {
                if (enumConstant.name == name) {
                    return enumConstant
                }
            }
        }
        throw RocketError("Enum constant not found")
    }
}