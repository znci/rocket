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
package dev.znci.rocket.scripting.classes

import dev.znci.rocket.Rocket
import dev.znci.twine.TwineTable
import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter

/**
 * Represents a command in the Rocket API, containing information about the command's name,
 * description, usage, permission, permission message, aliases, argument count, and the executor that handles the command.
 * This class encapsulates all necessary details to define and execute a command within the API.
 *
 * @param name The name of the command.
 * @param description A brief description of what the command does. Not shown to the player.
 * @param usage The usage information for the command, explaining how to use it.
 *              The message should be in MiniMessage format.
 * @param permission The permission required to execute the command.
 * @param permissionMessage The message shown when the user does not have the required permission to use the command.
 *                          The message should be in MiniMessage format.
 * @param aliases A list of alternative names for the command.
 * @param fallbackPrefix The prefix used for the command to override the default command prefix defined in the config.
 * @param argCount The number of arguments that the command expects.
 * @param executor The `CommandExecutor` responsible for executing the command logic.
 * @param tabCompleter The `TabCompleter` responsible for tab completions.
 */
data class CommandReference(
    var name: String = "",
    var description: String = "",
    var usage: String = "",
    var permission: String = "",
    var permissionMessage: String = "",
    var aliases: List<String> = listOf(),
    var fallbackPrefix: String = Rocket.instance.config.getString("command-fallback-prefix") ?: "rocket",
    var argCount: Int = 0,
    var executor: CommandExecutor = CommandExecutor { _, _, _, _ -> true },
    var tabCompleter: TabCompleter = TabCompleter { _, _, _, _ -> listOf() }
) : TwineTable("")