package dev.znci.rocket.util

object MessageFormatter {
    fun formatMessage(message: String): String {
        return message.replace("&", "§")
    }
}