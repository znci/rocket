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
package dev.znci.rocket.scripting.events

import com.google.common.reflect.ClassPath
import dev.znci.rocket.scripting.PlayerManager
import dev.znci.rocket.scripting.ScriptManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.luaj.vm2.Lua
import org.luaj.vm2.LuaBoolean
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction
import java.util.stream.Collectors


object EventListener : Listener {
    private val plugin: Plugin? = Bukkit.getPluginManager().getPlugin("rocket")

    fun registerAllEvents() {
        val eventClasses = getSupportedEvents()

        plugin?.logger?.info("Found ${eventClasses.size} events")
        for (eventClass in eventClasses) {
            try {
                if (plugin != null) {
                    plugin.logger.info("Registering event: ${eventClass.simpleName}")
                    Bukkit.getPluginManager().registerEvent(
                        eventClass,
                        this,
                        EventPriority.NORMAL,
                        { _, event ->
                            handleEvent(event)
                        },
                        plugin
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handleEvent(event: Event) {
        ScriptManager.usedEvents.forEach { (eventClass, callback) ->
            if (eventClass.isInstance(event)) {
                val luaTable = convertEventToLua(event)
                callback.call(luaTable)
            }
        }
    }

    private fun getSupportedEvents(): List<Class<out Event>> {
        return getBukkitEventClasses()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getBukkitEventClasses(): List<Class<out Event>> {
        return getClasses("org.bukkit.event")
            .filter { Event::class.java.isAssignableFrom(it) && it != Event::class.java }
            .map { it as Class<out Event> }
    }

    private fun getClasses(packageName: String): List<Class<*>> {
        return ClassPath.from(this::class.java.classLoader)
            .allClasses
            .filter { it.packageName.startsWith(packageName, ignoreCase = true) }
            .map { it.load() }
    }

    fun getEventByName(name: String): Class<out Event>? {
        return getSupportedEvents().find { it.simpleName.equals(name, true) }
    }

    private fun convertEventToLua(event: Event): LuaTable {
        val metaTable = LuaTable().apply {
            set("__index", object : TwoArgFunction() {
                override fun call(table: LuaValue, key: LuaValue): LuaValue {
                    // Player fields & checking
                    println(key.tojstring())
                    return when (key.tojstring()) {
                        "player" -> {
                            var player: org.bukkit.entity.Player? = null

                            // Check if there is a field for player
                            val playerField = event.javaClass.declaredFields.find { it.name == "player" }
                            if (playerField != null) {
                                playerField.isAccessible = true
                                val fieldPlayer = playerField.get(event)
                                if (fieldPlayer is org.bukkit.entity.Player) {
                                    player = fieldPlayer
                                }
                            }

                            // If there is no field for player, check if there is a method for player
                            if (player == null) {
                                val playerProperty = event.javaClass.methods.find { it.name == "getPlayer" }
                                if (playerProperty != null) {
                                    val playerFromProperty = playerProperty.invoke(event) as? org.bukkit.entity.Player
                                    player = playerFromProperty
                                }
                            }

                            if (player != null) {
                                return PlayerManager.getPlayerOverallTable(player)
                            }

                            return LuaValue.NIL
                        }
                        "hand" -> {
                            // Interaction event
                            return when (event) {
                                is org.bukkit.event.player.PlayerInteractEvent -> LuaValue.valueOf(event.hand.toString())
                                else -> LuaValue.NIL
                            }
                        }
                        "message" -> {
                            println(event.javaClass.simpleName)
                            return when (event) {
                                is org.bukkit.event.player.PlayerQuitEvent -> LuaValue.valueOf(event.quitMessage().toString())
                                is org.bukkit.event.player.PlayerJoinEvent -> LuaValue.valueOf(event.joinMessage().toString())
                                is org.bukkit.event.player.AsyncPlayerChatEvent -> LuaValue.valueOf(event.message).also {
                                    println(true)
                                    println(event.message)
                                }
                                is io.papermc.paper.event.player.AsyncChatEvent -> LuaValue.valueOf(event.message().toString())
                                else -> LuaValue.NIL
                            }
                        }
                        "reason" -> {
                            var reason: String? = null
                            when (event) {
                                is org.bukkit.event.player.PlayerQuitEvent -> LuaValue.valueOf(event.reason.toString())
                                else -> LuaValue.NIL
                            }

                        }

                        // FIXME: I may have broken the next two since I'm not sure how they work
                        //        I'm just trying to make it sort of fit the method this uses

                        "from" -> {
                            val fromField = event.javaClass.getDeclaredField("from")
                            fromField.isAccessible = true
                            val from = fromField.get(event)
                            if (from is org.bukkit.Location) {
                                // TODO: Finish this when mibers creates PR which adds new location class
                            }
                            LuaValue.NIL
                        }
                        "to" -> {
                            val toField = event.javaClass.getDeclaredField("to")
                            toField.isAccessible = true
                            val to = toField.get(event)
                            if (to is org.bukkit.Location) {
                                // TODO: Finish this when mibers creates PR which adds new location class
                            }
                            LuaValue.NIL
                        }
                        "cancel" -> {
                            // Cancellable events
                            when (event) {
                                is Cancellable -> {
                                    return object : ZeroArgFunction() {
                                        override fun call(): LuaValue {
                                            event.isCancelled = true
                                            return LuaBoolean.valueOf(event.isCancelled)
                                        }
                                    }
                                }

                                else -> LuaValue.NIL
                            }
                        }
                        else -> NIL
                    }
                }
            })
        }
        return metaTable
    }
}