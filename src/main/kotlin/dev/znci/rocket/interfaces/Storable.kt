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

interface Storable {
    fun toJson(): String
    companion object {
        fun fromJson(type: String, json: String): Storable {
            return try {
                val clazz = Class.forName(type)
                val method = clazz.getMethod("fromJson", String::class.java)
                method.invoke(null, json) as Storable
            } catch (e: Exception) {
                throw IllegalArgumentException("Failed to deserialize $type: ${e.message}", e)
            }
        }
    }
}