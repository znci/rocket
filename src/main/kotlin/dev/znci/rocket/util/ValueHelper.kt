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

import org.luaj.vm2.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class LuaValueHelper {
    fun constructFromLua(tableData: LuaTable, className: String): Any? {
        try {
            // Load the class dynamically
            val clazz = Class.forName(className).kotlin

            // Get the primary constructor and its parameters
            val constructor = clazz.primaryConstructor ?: return null
            val args = constructor.parameters.associateWith { param ->
                val luaValue = tableData.get(param.name)
                parseLuaValue(luaValue, param.type.jvmErasure) // Convert to correct type
            }

            // Create the instance with parsed arguments
            return constructor.callBy(args)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Parses Lua values (all strings) into the required Kotlin types.
     */
    fun parseLuaValue(value: LuaValue, targetType: KClass<*>): Any? {
        if (value.isnil()) return null

        return when (targetType) {
            String::class -> value.tojstring()
            Int::class -> value.tojstring().toIntOrNull() ?: 0
            Double::class -> value.tojstring().toDoubleOrNull() ?: 0.0
            Float::class -> value.tojstring().toFloatOrNull() ?: 0f
            Boolean::class -> value.tojstring().toBoolean() // "true" -> true, everything else -> false
            Long::class -> value.tojstring().toLongOrNull() ?: 0L
            Short::class -> value.tojstring().toShortOrNull() ?: 0
            Byte::class -> value.tojstring().toByteOrNull() ?: 0
            else -> null // Handle custom objects if needed
        }
    }

}



