package dev.znci.rocket.scripting.globals.tables.entities

import dev.znci.rocket.scripting.globals.interfaces.entities.Animals
import dev.znci.twine.nativex.TwineNative
import org.bukkit.entity.Cow

class LuaCowEntity(override val entity: Cow) : TwineNative(), Animals<Cow> {}