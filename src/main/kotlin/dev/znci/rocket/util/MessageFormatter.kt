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
package dev.znci.rocket.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

/**
 * The `MessageFormatter` object is responsible for formatting and deserializing messages.
 * It uses the MiniMessage library to convert a message string into a `Component` object,
 * allowing the message to be styled with color codes, special formatting, and other features.
 */
object MessageFormatter {
    /**
     * The minimessage instance for formatting.
     * This is used to deserialize without creating a new instance every time it is called.
     */
    private val miniMessage = MiniMessage.miniMessage()

    /**
     * Formats a message string into a `Component` object using the MiniMessage library.
     * The message can include special formatting syntax (e.g., color codes, placeholders).
     *
     * @param message The message string to format. It can contain MiniMessage formatting tags.
     * @return A `Component` object representing the formatted message.
     */
    fun formatMessage(message: String): Component {
        val messageComponent = miniMessage.deserialize(message)
        return messageComponent
    }

    /**
     * Serializes a `Component` object into a string using the MiniMessage library.
     * This allows the formatted message to be sent to players or logged.
     *
     * @return A string representation of the `Component`, formatted with MiniMessage syntax.
     */
    fun Component.toMiniMessage(): String {
        return miniMessage.serialize(this)
    }
}