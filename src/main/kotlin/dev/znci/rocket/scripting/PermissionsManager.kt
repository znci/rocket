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
package dev.znci.rocket.scripting

import org.bukkit.entity.Player

/**
 * The `PermissionsManager` object is responsible for managing player permissions and group memberships.
 * It provides utility functions to check if a player belongs to a specific group and if they have specific permissions.
 */
object PermissionsManager {
    /**
     * Checks if a player belongs to a specific group.
     *
     * This method checks whether the player has the permission for the specified group in the format `group.<groupName>`.
     *
     * @param player The player whose group is being checked.
     * @param group The name of the group to check.
     * @return `true` if the player belongs to the specified group, `false` otherwise.
     */
    fun isPlayerInGroup(player: Player, group: String): Boolean {
        return player.hasPermission("group.$group")
    }

    /**
     * Retrieves all the groups the player belongs to.
     *
     * This method checks all the player's effective permissions, filtering out those that are related to groups
     * (permissions starting with `group.`), and returns a list of group names.
     *
     * @param player The player whose groups are being retrieved.
     * @return A list of group names that the player is part of.
     */
    @Suppress("unused") // TODO: This will be used in the future. Remove this decorator when it's used.
    fun getPlayerGroups(player: Player): List<String> {
        return player.effectivePermissions
            .filter { it.permission.startsWith("group.") }
            .map { it.permission.substring(6) }
    }

    /**
     * Checks if a player has a specific permission.
     *
     * This method checks whether the player has the specified permission.
     *
     * @param player The player whose permissions are being checked.
     * @param permission The permission to check.
     * @return `true` if the player has the specified permission, `false` otherwise.
     */
    fun hasPermission(player: Player, permission: String): Boolean {
        return player.hasPermission(permission)
    }
}