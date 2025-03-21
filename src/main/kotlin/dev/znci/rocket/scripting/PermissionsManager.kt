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

object PermissionsManager {
    fun isPlayerInGroup(player: Player, group: String): Boolean {
        return player.hasPermission("group.$group")
    }

    @Suppress("unused") // TODO: This will be used in the future. Remove this decorator when it's used.
    fun getPlayerGroups(player: Player): List<String> {
        return player.effectivePermissions
            .filter { it.permission.startsWith("group.") }
            .map { it.permission.substring(6) }
    }

    fun hasPermission(player: Player, permission: String): Boolean {
        return player.hasPermission(permission)
    }
}