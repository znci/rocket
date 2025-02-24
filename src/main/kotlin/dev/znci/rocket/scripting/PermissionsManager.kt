package dev.znci.rocket.scripting

import org.bukkit.entity.Player

object PermissionsManager {
    fun isPlayerInGroup(player: Player, group: String): Boolean {
        return player.hasPermission("group.$group")
    }

    fun getPlayerGroups(player: Player): List<String> {
        return player.effectivePermissions
            .filter { it.permission.startsWith("group.") }
            .map { it.permission.substring(6) }
    }

    fun hasPermission(player: Player, permission: String): Boolean {
        return player.hasPermission(permission)
    }
}