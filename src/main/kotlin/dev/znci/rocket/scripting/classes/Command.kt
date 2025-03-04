package dev.znci.rocket.scripting.classes

import org.bukkit.command.CommandExecutor

data class Command(
    val name: String,
    var description: String,
    var usage: String,
    var permission: String,
    var aliases: List<String>,
    var executor: CommandExecutor
)