package dev.znci.rocket.scripting.events

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

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
        plugin?.logger?.info("Event: ${event.javaClass.simpleName}")
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

}