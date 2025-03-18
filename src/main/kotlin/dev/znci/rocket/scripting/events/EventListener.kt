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
import dev.znci.rocket.scripting.functions.LuaLocation
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.Plugin
import org.luaj.vm2.LuaBoolean
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction
import java.util.*
import kotlin.collections.HashSet

object EventListener : Listener {
    private val plugin: Plugin? = Bukkit.getPluginManager().getPlugin("rocket")

    lateinit var SUPPORTED_EVENTS: HashSet<Class<out Event>>
        private set

    fun registerEvent(eventClass: Class<out Event>) {
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

    fun cacheEvents() {
        SUPPORTED_EVENTS = getSupportedEvents()
    }

    private fun handleEvent(event: Event) {
        val eventCallbacks = ScriptManager.usedEvents[getEventByName(event.eventName)] ?: return
        eventCallbacks.forEach { callback ->
            val luaTable = convertEventToLua(event)
            callback.call(luaTable)
            println(callback)
        }
    }

    private fun getSupportedEvents(): HashSet<Class<out Event>> {
        val set = hashSetOf<Class<out Event>>()
        for (eventClass in getBukkitEventClasses()) {
            // TODO Add a debug setting to show these kinds of things
            //          (Similar to Skript's)
            println("Caching event ${eventClass.simpleName}")
            set.add(eventClass)
        }
        return set
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
        return SUPPORTED_EVENTS.find { it.simpleName.equals(name, true) }
    }

    private inline fun <reified V> getValueFromField(event: Event, valuesToTry: Array<out String>): V? {
        valuesToTry.forEach { value ->
            val field = event.javaClass.declaredFields.find { it.name == value }
            if (field != null) {
                field.isAccessible = true
                val valueFromField = field.get(event)
                if (valueFromField is V) {
                    return valueFromField
                }
            }
        }
        return null
    }

    private inline fun <reified V> getValueFromFunction(event: Event, valuesToTry: Array<out String>): V? {
        valuesToTry.forEach { value ->
            val valueProperty = event.javaClass.methods.find { it.name == value }
            if (valueProperty != null) {
                val valueFromProperty = valueProperty.invoke(event) as? V
                return valueFromProperty
            }
        }
        return null
    }

    private inline fun <reified V> getValueFromEvent(event: Event, vararg valuesToTry: String): V? {

        var valueObj: V? = getValueFromField(event, valuesToTry)
        if (valueObj == null) {
            valueObj = getValueFromFunction<V>(event, valuesToTry)
        }
        return valueObj
    }

    /**
     * Todo: Create a registry for extras, default to casting
     *          Maybe for a different PR, it depends on what znci is feeling
     */

    private fun convertEventToLua(event: Event): LuaTable {
        val table = LuaTable()
        val meta = table.getmetatable() ?: LuaTable()
        val indexFunction = meta.get("__index") as? TwoArgFunction
        meta.set("__index", object : TwoArgFunction() {
            override fun call(table: LuaValue, key: LuaValue): LuaValue {
                return when (key.tojstring()) {
                    "player" -> {
                        val player: Player? = getValueFromEvent(event, "player", "getPlayer")
                        if (player != null) {
                            return PlayerManager.getPlayerTable(player)
                        }
                        return LuaValue.NIL.also {
                            error("No player in a '${event.eventName}' event!")
                        }
                    }

                    "hand" -> {
                        val hand: EquipmentSlot? = getValueFromEvent(event, "hand")
                        if (hand != null) {
                            return LuaValue.valueOf(hand.toString())
                        }
                        return LuaValue.NIL.also {
                            error("No hand in a '${event.eventName}' event!")
                        }
                    }

                    "message" -> {
                        val message: Any? = getValueFromEvent(
                            event,
                            "message",
                            "getMessage",
                            "getJoinMessage",
                            "getDeathMessage",
                            "getQuitMessage",
                            "getKickMessage"
                        )
                        if (message != null) {
                            if (message is String || message is Component) return LuaValue.valueOf(message.toString())
                            return LuaValue.NIL.also {
                                error("Non-string/component object found in place of a message. Found '$message'")
                            }
                        }
                        return LuaValue.NIL.also {
                            error("No message in a '${event.eventName}' event!")
                        }
                    }

                    "from" -> {
                        val location: Location? = getValueFromEvent(event, "from", "getFrom")
                        return LuaLocation.fromBukkit(location?:return LuaValue.NIL.also {
                            error("Value 'from' of a '${event.eventName}' event returned null")
                        })
                    }

                    "to" -> {
                        val location: Location? = getValueFromEvent(event, "to", "getTo")
                        return LuaLocation.fromBukkit(location?:return LuaValue.NIL.also {
                            error("Value 'to' of a '${event.eventName}' event returned null")
                        })
                    }

                    "cancel" -> {
                        when (event) {
                            is Cancellable -> {
                                return object : ZeroArgFunction() {
                                    override fun call(): LuaValue {
                                        event.isCancelled = true
                                        return LuaBoolean.valueOf(event.isCancelled)
                                    }
                                }
                            }
                            else -> LuaValue.NIL.also {
                                error("Cannot access or modify 'cancel' field in an uncancellable '${event.eventName}' event")
                            }
                        }
                    }

                    else -> {

                        val value: Any? = getValueFromEvent(event, key.tojstring(),
                            "get${
                                key.tojstring()
                                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                            }")
                        if (value != null) return LuaValue.valueOf(value.toString())

                        indexFunction?.call(table, key) ?: LuaValue.NIL.also {
                            error("A '${event.eventName}' event has no member '${key.tojstring()}'")
                        }
                    }
                }
            }

        })
        table.setmetatable(meta)
        return table
    }

}