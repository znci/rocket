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