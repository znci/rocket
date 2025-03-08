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
     * Returns a LuaTable with general information about the player
     * This function is used for all the player getters.
     * Setters are in PlayerManager.getPlayerManagementTable
     *
     * @param player The player to get information from
     * @return LuaTable
     */
    fun getPlayerGeneralTable(player: Player): LuaTable {
        val table = LuaTable()

        table.set("name", LuaValue.valueOf(player.name))
        table.set("uuid", LuaValue.valueOf(player.uniqueId.toString()))
        table.set("health", LuaValue.valueOf(player.health))
        table.set("foodLevel", LuaValue.valueOf(player.foodLevel))
        table.set("gameMode", LuaValue.valueOf(player.gameMode.toString()))
        table.set("xp", LuaValue.valueOf(player.exp.toDouble()))
        table.set("level", LuaValue.valueOf(player.level))
        table.set("location", LuaValue.valueOf(player.location.toString()))
        table.set("world", LuaValue.valueOf(player.world.name))
        table.set("ip", LuaValue.valueOf(player.address?.hostString))
        table.set("isOp", LuaValue.valueOf(player.isOp))
        table.set("isFlying", LuaValue.valueOf(player.isFlying))
        table.set("isSneaking", LuaValue.valueOf(player.isSneaking))
        table.set("isSprinting", LuaValue.valueOf(player.isSprinting))
        table.set("isBlocking", LuaValue.valueOf(player.isBlocking))
        table.set("isSleeping", LuaValue.valueOf(player.isSleeping))

        val block = player.getTargetBlockExact(100)

        if (block != null) {
            table.set("targetBlockType", LuaValue.valueOf(block.type.toString()))
            table.set("targetBlockLocation", LuaValue.valueOf(block.location.toString()))
            table.set("targetBlockWorld", LuaValue.valueOf(block.world.name))
            table.set("targetBlockX", LuaValue.valueOf(block.x))
            table.set("targetBlockY", LuaValue.valueOf(block.y))
            table.set("targetBlockZ", LuaValue.valueOf(block.z))
            table.set("targetBlockLightLevel", LuaValue.valueOf(block.lightLevel.toDouble()))
            table.set("targetBlockTemperature", LuaValue.valueOf(block.temperature))
            table.set("targetBlockHumidity", LuaValue.valueOf(block.humidity))
        }

        return table
    }

    /**
     * Returns a LuaTable with functions to manage the player
     * This function is used for all the player setters.
     * Getters are in PlayerManager.getPlayerGeneralTable
     *
     * @param player The player to manage
     * @return LuaTable
     */
    fun getPlayerManagementTable(player: Player): LuaTable {
        val table = LuaTable()
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

        table.set("setXP", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                if (!value.isnumber()) return LuaValue.FALSE
                player.exp = value.tofloat()
                return LuaValue.TRUE
            }
        })

        table.set("setLevel", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                if (!value.isnumber()) return LuaValue.FALSE
                player.level = value.toint()
                return LuaValue.TRUE
            }
        })

        table.set("setHealth", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                if (!value.isnumber()) return LuaValue.FALSE
                player.health = value.todouble()
                return LuaValue.TRUE
            }
        })

        table.set("setFoodLevel", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                if (!value.isnumber()) return LuaValue.FALSE
                player.foodLevel = value.toint()
                return LuaValue.TRUE
            }
        })

        table.set("setSaturation", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                if (!value.isnumber()) return LuaValue.FALSE
                player.saturation = value.tofloat()
                return LuaValue.TRUE
            }
        })

        table.set("setExhaustion", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                if (!value.isnumber()) return LuaValue.FALSE
                player.exhaustion = value.tofloat()
                return LuaValue.TRUE
            }
        })

        table.set("setGameMode", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                if (!value.isnumber()) return LuaValue.FALSE
                player.gameMode = GameMode.entries.toTypedArray()[value.toint()]
                return LuaValue.TRUE
            }
        })

        table.set("setDisplayName", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                player.displayName(Component.text(value.tojstring()))
                return LuaValue.TRUE
            }
        })

        table.set("setTabListName", object : OneArgFunction() {
            override fun call(value: LuaValue): LuaValue {
                player.playerListName(Component.text(value.tojstring()))
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

        return table
    }

    /**
     * Returns a LuaTable with general and management information about the player
     *
     * @param player The player to get information from
     * @return LuaTable
     */
    fun getPlayerOverallTable(player: Player): LuaTable {
        val table = LuaTable()

        val generalTable = getPlayerGeneralTable(player)
        for (key in generalTable.keys()) {
            table.set(key, generalTable.get(key))
        }

        val managementTable = getPlayerManagementTable(player)
        for (key in managementTable.keys()) {
            table.set(key, managementTable.get(key))
        }

        return table
    }

}