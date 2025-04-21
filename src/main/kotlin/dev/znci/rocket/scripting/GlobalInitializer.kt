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
package dev.znci.rocket.scripting

import dev.znci.rocket.Rocket
import dev.znci.rocket.scripting.annotations.Global
import dev.znci.rocket.scripting.api.RocketError
import dev.znci.rocket.scripting.globals.tables.LuaCommands
import dev.znci.rocket.scripting.globals.tables.LuaHTTPClient
import dev.znci.rocket.scripting.globals.tables.LuaLocations
import dev.znci.rocket.scripting.globals.tables.LuaPlayers
import dev.znci.rocket.scripting.globals.tables.LuaServer
import dev.znci.rocket.scripting.globals.values.TestValue
import dev.znci.twine.TwineValueBase
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections

/**
 * The `GlobalInitializer` object is responsible for initializing and registering global objects
 * with the `ScriptManager`. It ensures that necessary components are properly registered for use
 * for the server admins.
 */
object GlobalInitializer {
    /**
     * Uses reflection to find all classes marked with `@Global`.
     *
     * If a non-TwineValueBase value is marked with `@Global`, it throws an error.
     */
    fun registerAll() {
        val reflections = Reflections("dev.znci.rocket")
        val globalClasses = reflections.getTypesAnnotatedWith(Global::class.java)

        globalClasses.forEach { globalClass ->
            if (TwineValueBase::class.java.isAssignableFrom(globalClass)) {
                val instance = globalClass.getDeclaredConstructor().newInstance() as TwineValueBase
                ScriptManager.registerGlobal(instance)
            } else {
                throw RocketError("@Global annotated class does not extend TwineValueBase.")
            }
        }

        Rocket.instance.logger.info { "Successfully registered ${globalClasses.size} globals." }
    }
}