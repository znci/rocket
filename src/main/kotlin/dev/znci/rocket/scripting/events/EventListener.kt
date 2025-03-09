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

import dev.znci.rocket.scripting.PlayerManager
import dev.znci.rocket.scripting.ScriptManager
import org.bukkit.Bukkit
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.Plugin
import org.luaj.vm2.LuaBoolean
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ZeroArgFunction


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
        return listOf(
            org.bukkit.event.player.PlayerJoinEvent::class.java,
            org.bukkit.event.block.BlockBreakEvent::class.java,
            org.bukkit.event.block.BlockPlaceEvent::class.java,
            org.bukkit.event.player.PlayerMoveEvent::class.java,
            org.bukkit.event.player.PlayerQuitEvent::class.java,
            org.bukkit.event.player.PlayerInteractEvent::class.java,
            io.papermc.paper.event.player.AsyncChatEvent::class.java
        )
    }

    fun getEventByName(name: String): Class<out Event>? {
        return getSupportedEvents().find { it.simpleName.equals(name, true) }
    }

    private fun convertEventToLua(event: Event): LuaTable {
        val luaTable = LuaTable()

        // Player fields & checking
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
            luaTable.set("player", PlayerManager.getPlayerOverallTable(player))
        }

        // Interaction event
        if (event is org.bukkit.event.player.PlayerInteractEvent) {
            val hand = event.hand
            luaTable.set("hand", hand.toString())
        }

        // Quit event
        if (event is org.bukkit.event.player.PlayerQuitEvent) {
            val quitMessage = event.quitMessage()
            luaTable.set("quitMessage", quitMessage.toString())

            val reason = event.reason
            luaTable.set("reason", reason.toString())
        }

        // Move event
        val fields = event.javaClass.declaredFields.map { it.name }
        if ("from" in fields) {
            val fromField = event.javaClass.getDeclaredField("from")
            fromField.isAccessible = true
            val from = fromField.get(event)
            if (from is org.bukkit.Location) {
                // TODO: Finish this when mibers creates PR which adds new location class
            }
        }

        if ("to" in fields) {
            val toField = event.javaClass.getDeclaredField("to")
            toField.isAccessible = true
            val to = toField.get(event)
            if (to is org.bukkit.Location) {
                // TODO: Finish this when mibers creates PR which adds new location class
            }
        }

        // Cancellable events
        if (event is Cancellable) {
            luaTable.set("cancel", object : ZeroArgFunction() {
                override fun call(): LuaValue {
                    event.isCancelled = true
                    return LuaBoolean.valueOf(event.isCancelled)
                }
            })
        }

        return luaTable
    }
}