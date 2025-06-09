package dev.znci.rocket.scripting.annotations

import org.bukkit.entity.EntityType

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Entity(val entity: EntityType)