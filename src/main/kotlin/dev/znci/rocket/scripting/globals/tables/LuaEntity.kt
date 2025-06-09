package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.scripting.EntityRegistry
import dev.znci.rocket.scripting.annotations.Global
import dev.znci.rocket.scripting.api.RocketError
import dev.znci.twine.nativex.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import kotlin.reflect.full.primaryConstructor

interface RocketEntity<out T : Entity> {
    val entity: T
}

@Suppress("unused")
@Global
class LuaEntities : TwineNative("entity") {
    @TwineNativeFunction
    fun new(entityType: String): RocketEntity<*> {
        val lowerType = entityType.lowercase()

        val bukkitType = EntityType.entries
            .find { it.name.equals(lowerType, ignoreCase = true) }
            ?: throw RocketError("Unsupported entity type: $entityType")

        val world = org.bukkit.Bukkit.getWorlds().first()

        val entityClass = bukkitType.entityClass ?: throw RocketError("Entity class not found for type: $entityType")
        val bukkitEntity = world.spawn(world.spawnLocation.add(0.0, 20.0, 0.0), entityClass)

        val wrapperClass = EntityRegistry.getWrapper(lowerType)
            ?: throw RocketError("No Lua wrapper registered for entity type: $entityType")

        val constructor = wrapperClass.primaryConstructor
            ?: throw RocketError("Wrapper class ${wrapperClass.simpleName} has no primary constructor")

        return constructor.call(bukkitEntity) as RocketEntity<*>
    }
}
