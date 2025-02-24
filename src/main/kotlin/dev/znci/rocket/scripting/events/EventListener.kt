package dev.znci.rocket.scripting.events

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
import org.luaj.vm2.lib.OneArgFunction
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
            luaTable.set("playerName", LuaValue.valueOf(player.name))
            luaTable.set("playerUUID", LuaValue.valueOf(player.uniqueId.toString()))
            luaTable.set("playerHealth", LuaValue.valueOf(player.health))
            luaTable.set("playerFoodLevel", LuaValue.valueOf(player.foodLevel))
            luaTable.set("playerGameMode", LuaValue.valueOf(player.gameMode.toString()))
            luaTable.set("playerXP", LuaValue.valueOf(player.exp.toDouble()))
            luaTable.set("playerLevel", LuaValue.valueOf(player.level))
            luaTable.set("playerLocation", LuaValue.valueOf(player.location.toString()))
            luaTable.set("playerWorld", LuaValue.valueOf(player.world.name))
            luaTable.set("playerIP", LuaValue.valueOf(player.address?.hostString))
            luaTable.set("playerIsOp", LuaValue.valueOf(player.isOp))
            luaTable.set("playerIsFlying", LuaValue.valueOf(player.isFlying))
            luaTable.set("playerIsSneaking", LuaValue.valueOf(player.isSneaking))
            luaTable.set("playerIsSprinting", LuaValue.valueOf(player.isSprinting))
            luaTable.set("playerIsBlocking", LuaValue.valueOf(player.isBlocking))
            luaTable.set("playerIsSleeping", LuaValue.valueOf(player.isSleeping))

            val block = player.getTargetBlockExact(5)

            if (block != null) {
                luaTable.set("targetBlockType", LuaValue.valueOf(block.type.toString()))
                luaTable.set("targetBlockLocation", LuaValue.valueOf(block.location.toString()))
                luaTable.set("targetBlockWorld", LuaValue.valueOf(block.world.name))
                luaTable.set("targetBlockX", LuaValue.valueOf(block.x))
                luaTable.set("targetBlockY", LuaValue.valueOf(block.y))
                luaTable.set("targetBlockZ", LuaValue.valueOf(block.z))
                luaTable.set("targetBlockLightLevel", LuaValue.valueOf(block.lightLevel.toDouble()))
                luaTable.set("targetBlockTemperature", LuaValue.valueOf(block.temperature))
                luaTable.set("targetBlockHumidity", LuaValue.valueOf(block.humidity))
            }
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