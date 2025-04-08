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
     * Formats a message string into a `Component` object using the MiniMessage library.
     * The message can include special formatting syntax (e.g., color codes, placeholders).
     *
     * @param message The message string to format. It can contain MiniMessage formatting tags.
     * @return A `Component` object representing the formatted message.
     */
    fun formatMessage(message: String): Component {
        val miniMessage = MiniMessage.miniMessage()
        val messageComponent = miniMessage.deserialize(message)
        return messageComponent
    }
}