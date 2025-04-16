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
package dev.znci.rocket.i18n

import dev.znci.rocket.util.MessageFormatter
import net.kyori.adventure.text.Component
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * LocaleManager is responsible for managing internationalization support in the plugin.
 * It loads language files from YAML configurations and provides the translated messages.
 */
object LocaleManager {
    /**
     * A map storing language keys and their corresponding translations.
     */
    private val messages = mutableMapOf<String, Map<String, String>>()

    /**
     * Default language code.
     */
    private const val DEFAULT_LANG: String = "en_GB"

    /**
     * The plugin instance.
     */
    private var plugin: JavaPlugin? = null

    /**
     * Current language being used.
     */
    private var lang: String = DEFAULT_LANG

    /**
     * Sets the plugin instance for LocaleManager.
     *
     * @param plugin The JavaPlugin instance.
     */
    fun setPlugin(plugin: JavaPlugin) {
        this.plugin = plugin
    }

    /**
     * Sets the current locale.
     *
     * @param lang The language code to switch to.
     */
    fun setLocale(lang: String) {
        if (messages.containsKey(lang)) {
            this.lang = lang
        }
    }

    /**
     * Loads all available language files from the plugin's locales directory.
     */
    fun loadLanguages() {
        val langFolder = File(plugin?.dataFolder, "locales")

        langFolder.listFiles()?.filter { it.extension == "yml" }?.forEach { file ->
            val langCode = file.nameWithoutExtension
            val config = YamlConfiguration.loadConfiguration(file)
            val messageMap = mutableMapOf<String, String>()

            config.getConfigurationSection("messages")?.getKeys(true)?.forEach { key ->
                val value = config.getString("messages.$key", "MISSING_TRANSLATION")!!
                messageMap[key] = value
            }

            messages[langCode] = messageMap
        }
    }

    /**
     * Retrieves a translated message for the given key.
     * If the message contains placeholders in {key} format, they will be replaced recursively.
     *
     * @param key The translation key.
     * @return The translated message or a fallback "MISSING_TRANSLATION" string.
     */
    private fun getMessage(key: String): String {
        val message = messages[lang]?.get(key)

        // regex check for {key} in message
        if (message != null && Regex("\\{.*?}").containsMatchIn(message)) {
            return message.replace(Regex("\\{.*?}")) {
                val messageKey = it.value.substring(1, it.value.length - 1)

                // make sure to format the message recursively
                getMessage(messageKey)
            }
        }

        return message ?: "MISSING_TRANSLATION"
    }

    /**
     * Retrieves a translated message as a Component with optional formatting arguments.
     *
     * @param key The translation key.
     * @param formatArgs Optional formatting arguments.
     * @return The translated message as a Component
     */
    fun getMessageAsComponent(key: String, vararg formatArgs: Any): Component {
        val message = getMessage(key)

        val formattedMessage = try {
            message.format(*formatArgs.map { it.toString() }.toTypedArray())
        } catch (e: Exception) {
            message
        }

        return MessageFormatter.formatMessage(formattedMessage)
    }
}