package dev.znci.rocket.scripting.globals

import dev.znci.rocket.scripting.annotations.RocketSimpleFunction
import dev.znci.rocket.scripting.annotations.RocketSimpleProperty
import dev.znci.rocket.scripting.api.RocketSimpleGlobal

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


class SimpleTest : RocketSimpleGlobal() {

    private var internalString = "Initial"

    @RocketSimpleProperty
    var stringRepresentation: String
        get() {
            println("Getter")
            return internalString
        }
        set(value) {
            println("Setter called with: $value")
            internalString = value.uppercase()  // just an example to showcase custom setter.
        }

    @RocketSimpleFunction
    fun testFunction(arg1: String): String {
        return "Hello, $arg1!"
    }
}



