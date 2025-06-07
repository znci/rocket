package dev.znci.rocket.scripting.globals.interfaces.entities

import org.bukkit.entity.LivingEntity

@Suppress("unused")
interface Creature<T> : Mob<T> where T : LivingEntity, T: org.bukkit.entity.Creature