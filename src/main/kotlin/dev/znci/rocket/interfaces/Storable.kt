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

package dev.znci.rocket.interfaces

import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions

interface Storable {
    fun toJson(): String
    companion object {
        fun fromJson(type: String, json: String): Storable {
            return try {
                val clazz = Class.forName(type)
                val method = clazz.kotlin.companionObject?.declaredFunctions?.firstOrNull { it.name == "fromJson" }
                    ?: throw IllegalArgumentException("No fromJson method found in companion object of $type")
                method.call(clazz.kotlin.companionObjectInstance, json) as Storable
            } catch (e: Exception) {
                throw IllegalArgumentException("Failed to deserialize $type: ${e.message}", e)
            }
        }
    }
}