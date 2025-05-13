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

import dev.znci.rocket.scripting.ScriptManager
import dev.znci.rocket.scripting.annotations.Global
import dev.znci.twine.TwineValueBase
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

open class RocketAddon : JavaPlugin() {
    private var className: String = this.javaClass.`package`.name

    fun onAddonEnable() {
        registerGlobals()
    }

    fun registerGlobals(): Int {
        val classLoader = this::class.java.classLoader
        val reflections = Reflections(
            ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(className, classLoader))
                .addClassLoaders(classLoader)
        )

        val globalClasses = reflections.getTypesAnnotatedWith(Global::class.java)

        val allTypes = reflections.getSubTypesOf(Any::class.java)
        globalClasses.forEach { globalClass ->
            if (TwineValueBase::class.java.isAssignableFrom(globalClass)) {
                val instance = globalClass.getDeclaredConstructor().newInstance() as TwineValueBase
                ScriptManager.registerGlobal(instance)
            } else {
                throw RocketError("@Global annotated class \"${globalClass.name}\" does not extend TwineValueBase.")
            }
        }

        return globalClasses.size
    }

    fun setClassName(className: String) {
        this.className = className
    }

    fun getClassName(): String = className
}