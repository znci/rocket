package dev.znci.rocket.scripting.globals.interfaces.entities

import org.bukkit.entity.LivingEntity

@Suppress("unused")
interface Mob<T> : dev.znci.rocket.scripting.globals.interfaces.entities.LivingEntity<T> where T: org.bukkit.entity.Creature