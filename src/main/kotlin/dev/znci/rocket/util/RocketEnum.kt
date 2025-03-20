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
package dev.znci.rocket.util

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.WorldType
import org.luaj.vm2.LuaTable

class RocketEnum(private val values: List<String>) {
    @Suppress("unused")
    fun getValues(): List<String> {
        return values
    }

    @Suppress("unused")
    fun isValid(key: String): Boolean {
        return values.contains(key)
    }

    fun getLuaTable(): LuaTable {
        val table = LuaTable()

        values.forEach() { value ->
            table.set(value, value)
        }

        return table
    }
}

fun <T : Enum<T>> enumToStringList(enumClass: Class<T>): List<String> {
    return enumClass.enumConstants.map { it.name }
}


data object RocketEnums {
    val RocketMaterial = RocketEnum(enumToStringList(Material::class.java))
    val RocketWorldType = RocketEnum(enumToStringList(WorldType::class.java))
    val RocketEnvironment = RocketEnum(enumToStringList(World.Environment::class.java))
}