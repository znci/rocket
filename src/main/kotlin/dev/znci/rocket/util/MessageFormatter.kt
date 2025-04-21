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
     * @param component The `Component` object to serialize.
     * @return A string representation of the `Component`, formatted with MiniMessage syntax.
     */
    fun Component.toMiniMessage(): String {
        return miniMessage.serialize(this)
    }
}