package dev.znci.rocket.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object MessageFormatter {
    fun formatMessage(message: String): Component {
        val miniMessage = MiniMessage.miniMessage()
        val messageComponent = miniMessage.deserialize(message)
        return messageComponent
    }
}