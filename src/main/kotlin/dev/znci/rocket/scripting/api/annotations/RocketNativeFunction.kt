
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
package dev.znci.rocket.scripting.api.annotations

/**
 * Annotation to mark a function as a native function in the Rocket framework.
 * This allows functions to be registered as callable from Lua.
 *
 * @param name The name of the function when it is added to a LuaTable.
 *             If not specified, the default name is "INHERIT_FROM_DEFINITION",
 *             which inherits the function name.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RocketNativeFunction(val name: String = "INHERIT_FROM_DEFINITION")