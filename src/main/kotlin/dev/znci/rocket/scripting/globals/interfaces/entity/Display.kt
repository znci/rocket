package dev.znci.rocket.scripting.globals.interfaces.entity

import dev.znci.rocket.scripting.api.RocketError
import dev.znci.rocket.scripting.classes.BaseLuaEntity
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import org.bukkit.Color

abstract class BaseDisplayEntity<T : org.bukkit.entity.Display>(entity: T):
    BaseLuaEntity<T>(entity),
    Display<T>

@Suppress("unused")
interface Display<T> : Entity<T> where T: org.bukkit.entity.Display {
    // TODO: Implement Transformation

    @TwineNativeFunction
    fun setInterpolationDuration(ticks: Int) {
        entity.interpolationDuration = ticks
    }

    @TwineNativeProperty
    val interpolationDuration: Int
        get() = entity.interpolationDuration

    @TwineNativeFunction
    fun setTeleportDuration(ticks: Int) {
        entity.teleportDuration = ticks
    }

    @TwineNativeProperty
    val teleportDuration: Int
        get() = entity.teleportDuration

    @TwineNativeFunction
    fun setViewRange(range: Float) {
        entity.viewRange = range
    }

    @TwineNativeProperty
    val viewRange: Float
        get() = entity.viewRange

    @TwineNativeFunction
    fun setShadowRadius(radius: Float) {
        entity.shadowRadius = radius
    }

    @TwineNativeProperty
    val shadowRadius: Float
        get() = entity.shadowRadius

    @TwineNativeFunction
    fun setWidth(width: Float) {
        entity.displayWidth = width
    }

    @TwineNativeProperty
    val displayWidth: Float
        get() = entity.displayWidth

    @TwineNativeFunction
    fun setHeight(height: Float) {
        entity.displayHeight
    }

    @TwineNativeProperty
    val displayHeight: Float
        get() = entity.displayHeight

    @TwineNativeFunction
    fun setInterpolationDelay(ticks: Int) {
        entity.interpolationDelay = ticks
    }

    @TwineNativeProperty
    val interpolationDelay: Int
        get() = entity.interpolationDelay

    // TODO: Implement Billboard
    @TwineNativeFunction
    fun setBillboard(billboard: String) {
        var billboardValue = org.bukkit.entity.Display.Billboard.CENTER

        billboardValue = when (billboard.lowercase()) {
            "vertical" -> org.bukkit.entity.Display.Billboard.VERTICAL
            "horizontal" -> org.bukkit.entity.Display.Billboard.HORIZONTAL
            "fixed" -> org.bukkit.entity.Display.Billboard.FIXED
            else -> throw RocketError("Invalid billboard value. (center/vertical/horizontal/fixed), got ${billboard.lowercase()}")
        }

        entity.billboard = billboardValue
    }

    @TwineNativeProperty
    val billboard: String
        get() = entity.billboard.toString()

    // TODO: Implement colors
    @TwineNativeFunction
    fun setGlowColor(r: Int, g: Int, b: Int) {
        entity.glowColorOverride = Color.fromRGB(r, g, b)
    }

    @TwineNativeProperty
    val glowColor: List<Int>
        get() = listOf(
            entity.glowColorOverride?.red ?: 0,
            entity.glowColorOverride?.green ?: 0,
            entity.glowColorOverride?.blue ?: 0
        )

    // TODO: Implement Brightness
}