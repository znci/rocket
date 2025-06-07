package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.scripting.annotations.Global
import dev.znci.rocket.scripting.api.RocketError
import dev.znci.rocket.scripting.globals.tables.entities.LuaCowEntity
import dev.znci.twine.nativex.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import org.bukkit.Bukkit
import org.bukkit.entity.Cow

//@Global
//enum class LuaEntitiesEnum {
//    Cow
//}

@Suppress("unused")
@Global
class LuaEntities : TwineNative("entity") {
    @TwineNativeFunction
    fun new(entityType: String): LuaCowEntity {
        when (entityType) {
            "cow" -> {
                val world = Bukkit.getWorlds().first()
                val cow = world.spawn(
                    world.spawnLocation.add(0.0, 20.0, 0.0),
                    Cow::class.java
                )
                return LuaCowEntity(cow)
            }
            else -> throw RocketError("Unsupported entity type: $entityType")
        }
    }
}
