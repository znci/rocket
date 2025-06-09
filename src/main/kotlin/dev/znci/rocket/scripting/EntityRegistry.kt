package dev.znci.rocket.scripting

import dev.znci.rocket.scripting.annotations.Entity
import dev.znci.rocket.scripting.globals.tables.RocketEntity
import org.reflections.Reflections
import kotlin.reflect.KClass

object EntityRegistry {
    val registry = mutableMapOf<String, KClass<out RocketEntity<*>>>()

    fun register(type: String, klass: KClass<out RocketEntity<*>>) {
        registry[type.lowercase()] = klass
    }

    fun getWrapper(type: String): KClass<out RocketEntity<*>>? {
        return registry[type.lowercase()]
    }

    fun autoRegisterEntities() {
        val reflections = Reflections("dev.znci.rocket.scripting.globals.tables.entities")
        val classes = reflections.getTypesAnnotatedWith(Entity::class.java)

        registry.clear()
        classes.forEach { clazz ->
            val entityAnnotation = clazz.getAnnotation(Entity::class.java)
            val entityTypeName = entityAnnotation.entity.name.lowercase()

            @Suppress("UNCHECKED_CAST")
            register(entityTypeName, clazz.kotlin as KClass<out RocketEntity<*>>)
        }
    }
}