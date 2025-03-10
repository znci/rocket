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

import dev.znci.rocket.scripting.functions.LuaLocation.Companion.fromBukkit
import dev.znci.rocket.scripting.functions.toBukkitLocation
import dev.znci.rocket.scripting.util.defineProperty
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

object PlayerManager {

    /**
     * Returns a LuaTable with general and management information about the player
     *
     *
     * @param player The player to get information from
     * @return LuaTable
     */
    fun getPlayerTable(player: Player): LuaTable {
        val table = LuaTable()

        // methods should be defined before properties
        table.set("send", object : OneArgFunction() {
            override fun call(message: LuaValue): LuaValue {
                val messageComponent = Component.text(
                    message.tojstring()
                        .replace("&", "§")
                )
                player.sendMessage(messageComponent)
                return LuaValue.TRUE
            }
        })

        table.set("addPermission", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                player.addAttachment(Bukkit.getPluginManager().getPlugin("Rocket")!!)
                    .setPermission(value.tojstring(), true)
                return LuaValue.TRUE
            }
        })

        table.set("op", object : ZeroArgFunction() {
            override fun call(): LuaValue {
                player.isOp = true
                return LuaValue.TRUE
            }
        })

        table.set("deop", object : ZeroArgFunction() {
            override fun call(): LuaValue {
                player.isOp = false
                return LuaValue.TRUE
            }
        })

        table.set("teleport", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                if (value is LuaTable) {
                    try {
                        val bukkitLocation = value.toBukkitLocation()
                        player.teleport(bukkitLocation)
                        return LuaValue.TRUE
                    } catch (e: Exception) {
                        return LuaValue.FALSE
                    }
                }
                return LuaValue.FALSE
            }
        })

        table.set("hasPermission", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                return LuaValue.valueOf(
                    PermissionsManager.hasPermission(player, value.tojstring())
                )
            }
        })

        table.set("isInGroup", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                return LuaValue.valueOf(
                    PermissionsManager.isPlayerInGroup(player, value.tojstring())
                )
            }
        })

        // read-only properties
        defineProperty(table, "name", { LuaValue.valueOf(player.name) })
        defineProperty(table, "uuid", { LuaValue.valueOf(player.uniqueId.toString()) })
        defineProperty(table, "world", { LuaValue.valueOf(player.world.name) })
        defineProperty(table, "ip", { LuaValue.valueOf(player.address?.hostString) })
        defineProperty(table, "isFlying", { LuaValue.valueOf(player.isFlying) })
        defineProperty(table, "isSneaking", { LuaValue.valueOf(player.isSneaking) })
        defineProperty(table, "isSprinting", { LuaValue.valueOf(player.isSprinting) })
        defineProperty(table, "isBlocking", { LuaValue.valueOf(player.isBlocking) })
        defineProperty(table, "isSleeping", { LuaValue.valueOf(player.isSleeping) })
        val block = player.getTargetBlockExact(100)
        if (block != null) {
            defineProperty(table, "targetBlockType", { LuaValue.valueOf(block.type.toString()) })
            defineProperty(table, "targetBlockLocation", { fromBukkit(block.location) })
            defineProperty(table, "targetBlockLightLevel", { LuaValue.valueOf(block.lightLevel.toDouble()) })
            defineProperty(table, "targetBlockTemperature", { LuaValue.valueOf(block.temperature) })
            defineProperty(table, "targetBlockHumidity", { LuaValue.valueOf(block.humidity) })
        }
        // writable properties
        defineProperty(
            table, "health",
            getter = { LuaValue.valueOf(player.health) },
            setter = { value -> player.health = value.todouble() },
            validator = { value -> value.isnumber() && value.todouble() >= 0 }
        )

        defineProperty(
            table, "foodLevel",
            getter = { LuaValue.valueOf(player.foodLevel) },
            setter = { value -> player.foodLevel = value.toint() },
            validator = { value -> value.isint() && value.toint() >= 0 && value.toint() <= 20 }
        )

        defineProperty(
            table, "gameMode",
            getter = { LuaValue.valueOf(player.gameMode.toString()) },
            setter = { value -> player.gameMode = GameMode.valueOf(value.tojstring()) },
            validator = { value ->
                value.isstring() && listOf("SURVIVAL", "CREATIVE", "ADVENTURE", "SPECTATOR").contains(value.tojstring())
            }
        )

        defineProperty(
            table, "xp",
            getter = { LuaValue.valueOf(player.exp.toDouble()) },
            setter = { value -> player.exp = value.tofloat() },
            validator = { value -> value.isnumber() && value.todouble() >= 0 && value.todouble() <= 1 }
        )

        defineProperty(
            table, "level",
            getter = { LuaValue.valueOf(player.level) },
            setter = { value -> player.level = value.toint() },
            validator = { value -> value.isint() && value.toint() >= 0 }
        )

        defineProperty(
            table, "location",
            getter = { fromBukkit(player.location) },
            setter = { value ->
                player.teleport(value.toBukkitLocation())
            },
            validator = { value ->
                value is LuaTable && runCatching { value.toBukkitLocation() }.isSuccess
            }
        )

        defineProperty(
            table, "isOp",
            getter = { LuaValue.valueOf(player.isOp) },
            setter = { value -> player.isOp = value.toboolean() },
            validator = { value -> value.isboolean() }
        )

        defineProperty(
            table, "saturation",
            getter = { LuaValue.valueOf(player.saturation.toDouble()) },
            setter = { value -> player.saturation = value.tofloat() },
            validator = { value -> value.isnumber() && value.todouble() >= 0 }
        )

        defineProperty(
            table, "exhaustion",
            getter = { LuaValue.valueOf(player.exhaustion.toDouble()) },
            setter = { value -> player.exhaustion = value.tofloat() },
            validator = { value -> value.isnumber() && value.todouble() >= 0 }
        )

        defineProperty(
            table, "displayName",
            getter = { LuaValue.valueOf(player.displayName().toString()) },
            setter = { value -> player.displayName(Component.text(value.tojstring())) },
            validator = { value -> value.isstring() }
        )

        defineProperty(
            table, "tabListName",
            getter = { LuaValue.valueOf(player.playerListName().toString()) },
            setter = { value -> player.playerListName(Component.text(value.tojstring())) },
            validator = { value -> value.isstring() }
        )

        return table
    }
}