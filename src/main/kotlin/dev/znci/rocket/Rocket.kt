package dev.znci.rocket

import dev.znci.rocket.commands.RocketCommand
import dev.znci.rocket.scripting.events.EventListener
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Rocket : JavaPlugin() {

    override fun onEnable() {
        val pluginDataFolder = this.dataFolder
        val scriptsFolder = File(pluginDataFolder, "scripts")

        if (!scriptsFolder.exists()) {
            scriptsFolder.mkdirs()
        }

        this.getCommand("rocket")?.setExecutor(RocketCommand(this))

        logger.info("Rocket plugin enabled")
        EventListener.registerAllEvents()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
