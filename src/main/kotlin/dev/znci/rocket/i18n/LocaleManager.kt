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

object LocaleManager {
    private val messages = mutableMapOf<String, Map<String, String>>()
    private val defaultLang: String = "en_GB"

    private var plugin: JavaPlugin? = null
    private var lang: String = defaultLang

    fun setPlugin(plugin: JavaPlugin) {
        this.plugin = plugin
    }

    fun setLocale(lang: String) {
        if (messages.containsKey(lang)) {
            this.lang = lang
        }
    }

    fun loadLanguages() {
        val langFolder = File(plugin?.dataFolder, "locales")
        if (!langFolder.exists()) langFolder.mkdirs()

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

    fun getMessage(key: String): String {
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

    fun getMessageAsComponent(key: String, vararg formatArgs: Any): Component {
        val message = getMessage(key)

        val formattedMessage = try {
            message.format(*formatArgs.map { it.toString() }.toTypedArray())
        } catch (e: Exception) {
            message
        }

        return Component.text(
            MessageFormatter.formatMessage(formattedMessage)
        )
    }
}