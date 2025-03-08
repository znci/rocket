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
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
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
        )
    }

    fun getEventByName(name: String): Class<out Event>? {
        return getSupportedEvents().find { it.simpleName.equals(name, true) }
    }

    private fun convertEventToLua(event: Event): LuaTable {
        val luaTable = LuaTable()

        val playerField = event.javaClass.getDeclaredFields().find { it.name == "player" }
        playerField?.isAccessible = true
        val player = playerField?.get(event)

        if (player is org.bukkit.entity.Player) {
            luaTable.set("player", PlayerManager.getPlayerOverallTable(player))
        }

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