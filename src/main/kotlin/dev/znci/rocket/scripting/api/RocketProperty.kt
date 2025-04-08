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

/**
 * Represents a property in the Rocket framework, extending the `RocketValueBase` class.
 * This class provides a basic structure for holding a property with a `valueName` and a `value`.
 * It is used to represent properties that can be accessed and modified (getters/setters).
 *
 * @param valueName The name of the property.
 */
open class RocketProperty(
    override var valueName: String,
) : RocketValueBase(valueName) {
    /**
     * The value of the property.
     * This is a generic any field that can hold any type of data.
     * It must be assigned before use.
     */
    lateinit var value: Any
}