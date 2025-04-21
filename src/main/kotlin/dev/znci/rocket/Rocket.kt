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
package dev.znci.rocket

import dev.znci.rocket.commands.RocketCommand
import dev.znci.rocket.i18n.LocaleManager
import dev.znci.rocket.scripting.ScriptManager
import dev.znci.rocket.scripting.events.EventListener
import dev.znci.rocket.scripting.GlobalInitializer
import dev.znci.rocket.scripting.annotations.Global
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import java.io.File

open class Rocket : JavaPlugin() {
    companion object {
        lateinit var instance: Rocket
            private set
    }

    private var defaultLocale: String = "en_GB"

    override fun onEnable() {
        instance = this

        // Create the plugin data folder
        saveDefaultConfig()

        // Load the default locales or the one specified in the config
        defaultLocale = config.getString("locale", "en_GB").toString()

        // Set the plugin and the default locale
        LocaleManager.setPlugin(this)
        LocaleManager.setLocale(defaultLocale)

        // Copy locale files from resources to the locales plugin data folder
        val defaultLocales = arrayOf("en_GB")
        defaultLocales.forEach {
            saveResource("locales/$it.yml", false)
        }

        LocaleManager.loadLanguages()

        // Create the scripts folder
        val pluginDataFolder = this.dataFolder
        val scriptsFolder = File(pluginDataFolder, "scripts")

        if (!scriptsFolder.exists()) {
            scriptsFolder.mkdirs()
        }

        ScriptManager.scriptsFolder = scriptsFolder

        // Register commands
        this.getCommand("rocket")?.setExecutor(RocketCommand(this))

        // Register all events
        EventListener.registerAllEvents()

        // Register globals
        GlobalInitializer.registerAll()

        // Automatically load all scripts in the scripts folder
        ScriptManager.loadScripts()

        logger.info("Rocket plugin enabled")
    }

    override fun onDisable() {
        logger.info("Rocket plugin disabled")
    }
}
