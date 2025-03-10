package dev.znci.rocket.scripting.util

import org.bukkit.Bukkit
import org.bukkit.World
import java.util.*

fun getWorldByNameOrUUID(worldNameOrUUID: String): World {
    return Bukkit.getWorld(worldNameOrUUID) ?:
    try {
        Bukkit.getWorld(UUID.fromString(worldNameOrUUID))
    } catch (e: IllegalArgumentException) {
        null
    } ?: error("World '$worldNameOrUUID' not found!")
}