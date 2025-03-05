package dev.znci.rocket

import dev.znci.rocket.commands.RocketCommand
import dev.znci.rocket.i18n.LocaleManager
import dev.znci.rocket.scripting.events.EventListener
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Rocket : JavaPlugin() {
    var defaultLocale: String = "en_GB"

    override fun onEnable() {
        // Create the plugin data folder
        saveDefaultConfig()

        // Load the default locales or the one specified in the config
        defaultLocale = config.getString("locale", "en_GB").toString()

        // Set the plugin and the default locale
        LocaleManager.setPlugin(this)
        LocaleManager.setLocale(defaultLocale)

        LocaleManager.loadLanguages()

        // Create the scripts folder
        val pluginDataFolder = this.dataFolder
        val scriptsFolder = File(pluginDataFolder, "scripts")

        if (!scriptsFolder.exists()) {
            scriptsFolder.mkdirs()
        }

        // Register commands
        this.getCommand("rocket")?.setExecutor(RocketCommand(this))

        // Register all events
        logger.info("Rocket plugin enabled")
        EventListener.registerAllEvents()
    }

    override fun onDisable() {
        logger.info("Rocket plugin disabled")
    }
}
