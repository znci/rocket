package dev.znci.rocket.scripting.classes

import dev.znci.rocket.scripting.globals.tables.RocketEntity
import dev.znci.twine.nativex.TwineNative
import org.bukkit.entity.Entity

abstract class BaseLuaEntity<T : Entity>(
    final override val entity: T
) : TwineNative(), RocketEntity<T>